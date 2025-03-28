package com.NonEstArsMea.agz_time_table.present.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.VelocityTracker
import android.view.View
import android.widget.OverScroller
import androidx.core.graphics.drawable.toDrawable
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.util.getStaticLayout
import com.NonEstArsMea.agz_time_table.util.wrapText
import com.google.android.material.color.MaterialColors
import kotlin.math.max

class NewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    // Основная информация об строках и колонках
    private val minCellHeight = 120f
    private val minCellWidth = 150f


    private var startScaleFactor: Float? = null


    private var contentWidth = width
    private var contentHeight = height

    // Отвечает за зум и сдвиги
    private var transformations = Transformations()

    // Значения последнего эвента
    private val lastPoint = PointF()
    private var lastPointerId = 0

    private val timeStartOfLessonsList = listOf(
        "9:00", "10:45", "12:30", "14:45", "16:25", "18:05"
    )
    private val timeEndOfLessonsList = listOf(
        "10:30", "12:15", "14:00", "16:15", "17:55", "19:35"
    )


    private val namesOfWeekDay = listOf(
        "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ"
    )

    // Переменные для отрисовки строк

    private var rowHeadersStaticLayoutList = mutableListOf<CustomRect>()
    private var columnHeadersStaticLayoutList = mutableListOf<CustomRect>()
    private var lessonsStaticLayoutList = mutableListOf<CustomRect>()

    // В эти перемемнные сохраняются ширина и высота ячеек,
    // т.к. они могут меняться из-за изменяемоего контента

    private val masOfCellWidth = mutableListOf<Float>()
    private val masOfCellHeight = mutableListOf<Float>()


    override fun setBackground(background: Drawable?) {
        super.setBackground(
            MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorSurfaceContainer,
                Color.WHITE
            ).toDrawable()
        )
    }


    private val separatorsPaint = Paint().apply {
        strokeWidth = 2f
        color = MaterialColors.getColor(
            context,
            com.google.android.material.R.attr.colorOnSurfaceVariant,
            Color.WHITE
        )
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            contentWidth
        } else {
            // Даже если AT_MOST занимаем все доступное место, т.к. может быть зум
            MeasureSpec.getSize(widthMeasureSpec)
        }

        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            // Нас никто не ограничивает - занимаем размер контента
            MeasureSpec.UNSPECIFIED -> heightSpecSize
            // Ограничение "не больше, не меньше" - занимаем столько, сколько пришло в спеке
            MeasureSpec.EXACTLY -> heightSpecSize
            // Можно занять меньше места, чем пришло в спеке, но не больше
            MeasureSpec.AT_MOST -> heightSpecSize.coerceAtMost(heightSpecSize)
            // Успокаиваем компилятор, сюда не попадем
            else -> error("Unreachable")
        }

        setMeasuredDimension(width, height)
    }


    // Отрисовка конкретных данных в строках
    private fun Canvas.drawTable() {


        rowHeadersStaticLayoutList.forEachIndexed { index, cell ->

            drawLine(
                /* startX = */ 0f,
                /* startY = */
                (masOfCellHeight.listSum(index + 1) * transformations.scaleFactor + transformations.translationY),
                /* stopX = */
                width.toFloat(),
                /* stopY = */
                (masOfCellHeight.listSum(index + 1) * transformations.scaleFactor + transformations.translationY),
                /* paint = */
                separatorsPaint
            )

            (cell as NamesRect).updateInitialRect(
                0,
                (masOfCellHeight.listSum(index + 1).toInt()),
            )
            if (cell.isRectVisible) {
                cell.draw(this)
            }
        }

        columnHeadersStaticLayoutList.forEachIndexed { index, cell ->

            drawLine(
                /* startX = */ (masOfCellWidth.listSum(index + 1) * transformations.scaleFactor + transformations.translationX),
                /* startY = */ 0f,
                /* stopX = */
                (masOfCellWidth.listSum(index + 1) * transformations.scaleFactor + transformations.translationX),
                /* stopY = */
                height.toFloat(),
                /* paint = */
                separatorsPaint
            )

            (cell as NamesRect).updateInitialRect(
                masOfCellWidth.listSum(index + 1).toInt(),
                0
            )
            if (cell.isRectVisible) {
                cell.draw(this)
            }
        }

        lessonsStaticLayoutList.forEachIndexed { index, cell ->
            (cell as LessonsRect).updateInitialRect(
                masOfCellWidth.listSum(cell.column).toInt(),
                masOfCellHeight.listSum(cell.row).toInt(),
                masOfCellWidth[cell.column],
                masOfCellHeight[cell.row]
            )

            if (cell.isRectVisible) {
                cell.draw(this)
            }
        }

        //Проверка на выполнение один раз

        if (startScaleFactor == null) {
            startScaleFactor =
                height.toFloat() / contentHeight.toFloat() * transformations.scaleFactor
            transformations.scaleFactor = startScaleFactor!!
            transformations.minScaleFactor = startScaleFactor!!
            invalidate()
        }

    }


    override fun onDraw(canvas: Canvas) = with(canvas) {
        drawTable()
    }


    fun setTimeTable(
        timeTable: List<List<CellClass>> = emptyList(),
        dateList: List<String> = emptyList()
    ) {

        if (timeTable.isNotEmpty()) {
            // Очистка массива
            transformations.resetTranslation()
            clearMas()
            val listOfLists = timeTable.map { listOfCell ->
                listOfCell.filter {
                    it.text == ""
                }
            }
            val list = listOfLists.flatten()

            val columnNameTextList = buildList {
                for (a in namesOfWeekDay.indices) {
                    add(DataForCellClass(namesOfWeekDay[a], dateList[a]))
                }
            }

            val rowNameText = buildList {
                for (a in timeStartOfLessonsList.indices) {
                    add(DataForCellClass(timeStartOfLessonsList[a], timeEndOfLessonsList[a]))
                }
            }


            calculateTable(list, columnNameTextList, rowNameText, CLASSROOM_INFO_TYPE)
            invalidate()
        }

    }

    fun setDateTable(
        list: List<CellClass>,
        unicList: List<String>
    ) {
        transformations.resetTranslation()
        clearMas()
        val columnNameTextList = buildList {
            for (a in unicList.indices) {
                add(DataForCellClass(unicList[a], ""))
            }
        }

        val rowNameText = buildList {
            for (a in timeStartOfLessonsList.indices) {
                add(DataForCellClass(timeStartOfLessonsList[a], timeEndOfLessonsList[a]))
            }
        }

        list.forEach { it.color = R.color.blue_fo_lessons_card }

        calculateTable(list, columnNameTextList, rowNameText, TEACHER_AND_GROOP_INFO_TYPE)
        invalidate()
    }

    fun clearView() {
        clearMas()
        invalidate()
    }


    fun setCafTimeTable(
        map: Map<String, List<CellClass>>,
        unicList: List<String>
    ) {

        if (map.isNotEmpty()) {
            transformations.resetTranslation()
            clearMas()
            val columnNameTextList = buildList {
                for (a in unicList.indices) {
                    add(DataForCellClass(unicList[a], ""))
                }
            }


            val rowNameText = buildList {
                for (a in timeStartOfLessonsList.indices) {
                    add(DataForCellClass(timeStartOfLessonsList[a], timeEndOfLessonsList[a]))
                }
            }

            unicList.forEachIndexed { index, it ->
                map[it]!!.forEach {
                    it.column = index + 1
                }

            }

            val list = map.values.flatten()

            list.forEach {
                it.color = R.color.orange_fo_lessons_card
            }

            calculateTable(list, columnNameTextList, rowNameText, TEACHER_AND_GROOP_INFO_TYPE)
            invalidate()
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        return if (event.pointerCount > 1) scaleDetector.onTouchEvent(event) else processMove(event)
    }

    private val scaleDetector = ScaleGestureDetector(
        context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                return run {
                    transformations.addScale(detector.scaleFactor)
                    true
                }
            }
        })


    private val scroller = OverScroller(context)
    private var velocityTracker = VelocityTracker.obtain()
    private fun processMove(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                velocityTracker?.recycle() // Освобождаем старый трекер
                velocityTracker = VelocityTracker.obtain() // Новый трекер
                velocityTracker?.addMovement(event)

                if (!scroller.isFinished) scroller.abortAnimation()
                lastPoint.set(event.x, event.y)
                lastPointerId = event.getPointerId(0)
                true
            }

            MotionEvent.ACTION_MOVE -> {

                velocityTracker?.addMovement(event)

                // Если размер контента меньше размера View - сдвиг недоступен
                if (width < contentWidth * transformations.scaleFactor) {
                    val pointerId = event.getPointerId(0)
                    // Чтобы избежать скачков - сдвигаем, только если поинтер(палец) тот же, что и раньше
                    if (lastPointerId == pointerId) {
                        transformations.addTranslation(event.x - lastPoint.x, event.y - lastPoint.y)

                    }

                    // Запоминаем поинтер и последнюю точку в любом случае
                    lastPoint.set(event.x, event.y)
                    lastPointerId = event.getPointerId(0)

                    true
                } else {
                    false
                }
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker?.addMovement(event)
                velocityTracker?.computeCurrentVelocity(1000, 5000f )

                val velocityX = velocityTracker.xVelocity
                val velocityY = velocityTracker.yVelocity
                Log.e("cust2", velocityX.toString() + " " + velocityY.toString())
                startFling(velocityX.toInt(), velocityY.toInt())

                velocityTracker?.recycle() // Освобождаем ресурсы
                velocityTracker = null
                true
            }

            else -> false
        }
    }

    private fun startFling(velocityX: Int, velocityY: Int) {
         scroller.fling(
            transformations.translationX.toInt(),
            transformations.translationY.toInt(),
            velocityX , velocityY ,
             (-contentWidth * transformations.scaleFactor).toInt(),
             0,
            -(contentHeight* transformations.scaleFactor).toInt(),
             0

        )
        postInvalidateOnAnimation()
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            Log.e("cust_scroll", scroller.currX.toString() + " " + scroller.currY.toString())
            Log.e("cust_scroll", transformations.translationX.toString() + " " + transformations.translationY.toString())
            val dx = (scroller.currX - transformations.translationX).toFloat()
            val dy = (scroller.currY - transformations.translationY).toFloat()

            transformations.addTranslation(dx, dy)
            postInvalidateOnAnimation()
        }
    }

    private inner class Transformations {

        var scaleFactor = 1.0f
        var minScaleFactor = 0.05f
        private var maxScaleFactor = 20.0f

        var translationX = 0f
            private set
        var translationY = 0f
            private set

        // На сколько максимально можно сдвинуть диаграмму
        private val minTranslationX: Float
            get() = (width - contentWidth * scaleFactor).coerceAtMost(0f)
        private val minTranslationY: Float
            get() = (height - (contentHeight * transformations.scaleFactor).toInt()).coerceAtMost(0)
                .toFloat()

        fun addTranslation(dx: Float, dy: Float) {
            translationX = (translationX + dx).coerceIn(minTranslationX, 0f)
            translationY = (translationY + dy).coerceIn(minTranslationY, 0f)
            invalidate()
        }

        fun resetTranslation() {
            translationX = 0f
            translationY = 0f
            scaleFactor = 1.0f
            invalidate()
        }

        fun addScale(sx: Float) {
            if (sx > 1) {
                if (scaleFactor < maxScaleFactor) {
                    scaleFactor = (scaleFactor * sx).coerceIn(minScaleFactor, maxScaleFactor)
                    translationX = (translationX * sx).coerceIn(minTranslationX, 0f)
                    translationY = (translationY * sx).coerceIn(minTranslationY, 0f)
                    invalidate()
                }

            } else {
                if (scaleFactor > minScaleFactor && (height < contentHeight * transformations.scaleFactor)) {
                    scaleFactor = (scaleFactor * sx).coerceIn(minScaleFactor, maxScaleFactor)
                    translationX = (translationX * sx).coerceIn(minTranslationX, 0f)
                    translationY = (translationY * sx).coerceIn(minTranslationY, 0f)
                    invalidate()
                }
            }

        }
    }


    private open inner class CustomRect()

    private inner class LessonsRect(
        private val text: DataForCellClass,
        val row: Int,
        val column: Int,
        val newColor: Int
    ) : CustomRect() {


        // Создание прямоуголька и задание радиуса и размера боковой линии
        private var rect = RectF()
        private var rectRadius = 10f * transformations.scaleFactor
        private var verticalLineSize = 10f * transformations.scaleFactor
        private val rectStrokeWidth = 2f


        @SuppressLint("ResourceType")
        private val strokePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = rectStrokeWidth
            color = resources.getColor(newColor, null)
        }

        private val paddingX = 15f
        private val paddingY = 15f
        private val margin = 15f
        private val path = Path()
        private val path2 = Path()

        private var calculatedWidth = minCellWidth
        private var calculatedHeight = minCellHeight

        private val nameTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = resources.getDimension(R.dimen.lesson_name_text_size)
            isFakeBoldText = true
            color = MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.titleTextColor,
                Color.WHITE
            )
        }
        private val infoTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = resources.getDimension(R.dimen.lesson_info_text_size)
            isFakeBoldText = false
            color = MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorOnSurfaceVariant,
                Color.WHITE
            )
        }

        fun calculateSize() {
            val words = text.text.split(" ")
            val infoWords = text.infoText.split("\n")
            val maxWordWith = max(
                words.maxOf { nameTextPaint.measureText(it) },
                infoWords.maxOf { infoTextPaint.measureText(it) }
            )
            val textHeight =
                nameTextPaint.fontMetrics.run { bottom - top.toInt() }
            val infoTextHeight = infoTextPaint.fontMetrics.run { bottom - top.toInt() }
            val lines = wrapText(text.text, maxWordWith, nameTextPaint)
            val infoLines = wrapText(text.infoText, maxWordWith, infoTextPaint)

            calculatedWidth = max(
                minCellWidth,
                maxWordWith + margin * 2 + verticalLineSize + rectStrokeWidth * 2 + paddingX * 2
            )
            calculatedHeight =
                max(
                    minCellHeight,
                    lines.size * textHeight + infoLines.size * infoTextHeight + margin * 2 + rectStrokeWidth * 2 + paddingY * 2
                )

            updateStaticLayouts()
        }

        fun getCalculatedWidth() = calculatedWidth
        fun getCalculatedHeight() = calculatedHeight


        // пэйнтдля фона
        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.TRANSPARENT
            strokeWidth = 0f
            isAntiAlias = true
        }


        private var nameStaticLayout: StaticLayout? = null
        private var infoStaticLayout: StaticLayout? = null

        private fun updateStaticLayouts() {
            nameStaticLayout = getStaticLayout(
                text = text.text,
                width = calculatedWidth - (margin * 2 + verticalLineSize + rectStrokeWidth * 2 + paddingX * 2),
                paint = nameTextPaint,
                true
            )
            infoStaticLayout = getStaticLayout(
                text = text.infoText,
                width = calculatedWidth - (margin * 2 + verticalLineSize + rectStrokeWidth * 2 + paddingX * 2),
                paint = infoTextPaint,
                true
            )
        }

        // Начальный Rect для текущих размеров View
        private val untransformedRect = RectF()

        // Если false, таск рисовать не нужно
        val isRectVisible: Boolean
            get() = (rect.top < this@NewView.height) and (rect.bottom >= 0) and (rect.left < this@NewView.width) and (rect.right > 0)


        fun updateInitialRect(x: Int, y: Int, width: Float, height: Float) {
            fun getY(): Float {
                return (y + margin) * transformations.scaleFactor + transformations.translationY
            }

            fun getX(): Float {
                return (x + margin) * transformations.scaleFactor + transformations.translationX
            }

            fun getEndX(): Float {
                return (x + width - margin) * transformations.scaleFactor + transformations.translationX
            }

            fun getEndY(): Float {
                return (y + calculatedHeight - margin) * transformations.scaleFactor + transformations.translationY
            }

            untransformedRect.set(
                getX(),
                getY(),
                getEndX(),
                getEndY()
            )

            rect.set(untransformedRect)
        }


        fun draw(canvas: Canvas) {
            path.reset()
            path2.reset()

            paint.color = resources.getColor(changeColor(newColor), null)
            // создаем обводку для
            val strokeRect = RectF(
                rect.left,
                rect.top,
                rect.right,
                rect.bottom
            )
            // Отрисовка самого прямоугольника
            path.addRoundRect(rect, rectRadius, rectRadius, Path.Direction.CW)
            canvas.drawPath(path, paint)

            paint.color = resources.getColor(newColor, null)
            // Отрисовка боковой части
            with(path) {
                reset()
                addRoundRect(
                    rect.left,
                    rect.top,
                    rect.right,
                    rect.bottom,
                    rectRadius,
                    rectRadius,
                    Path.Direction.CW
                )
            }

            with(path2) {
                reset()
                addRect(
                    rect.left + verticalLineSize,
                    rect.top,
                    rect.right,
                    rect.bottom, Path.Direction.CW
                )
                op(path, Path.Op.REVERSE_DIFFERENCE)
            }

            canvas.drawPath(path2, paint)

            canvas.drawRoundRect(strokeRect, rectRadius, rectRadius, strokePaint)

            canvas.save()
            canvas.translate(
                rect.left + verticalLineSize + paddingX + rectStrokeWidth,
                rect.top + (rect.bottom - rect.top - (nameStaticLayout!!.height + infoStaticLayout!!.height) * transformations.scaleFactor) / 2
            )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            nameStaticLayout!!.draw(canvas)
            canvas.restore()

            canvas.save()
            canvas.translate(
                rect.left + verticalLineSize + paddingX + rectStrokeWidth,
                rect.top + (rect.bottom - rect.top - (infoStaticLayout!!.height - nameStaticLayout!!.height) * transformations.scaleFactor) / 2
            )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            infoStaticLayout!!.draw(canvas)
            canvas.restore()

        }


        private fun changeColor(newColor: Int): Int {
            return when (newColor) {
                R.color.red_fo_lessons_card -> R.color.red_fo_lessons_card_alpha
                R.color.yellow_fo_lessons_card -> R.color.yellow_fo_lessons_card_alpha
                R.color.green_fo_lessons_card -> R.color.green_fo_lessons_card_alpha
                R.color.blue_fo_lessons_card -> R.color.blue_fo_lessons_card_alpha
                R.color.orange_fo_lessons_card -> R.color.orange_fo_lessons_card_alpha
                else -> {
                    R.color.white
                }
            }

        }

    }

    private inner class NamesRect(
        private val text: DataForCellClass,
    ) : CustomRect() {
        private var calculatedWidth = minCellWidth
        private var calculatedHeight = minCellHeight

        private val nameTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = resources.getDimension(R.dimen.lesson_name_text_size)
            isFakeBoldText = true
            color = MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.titleTextColor,
                Color.WHITE
            )
        }
        private val infoTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = resources.getDimension(R.dimen.lesson_info_text_size)
            isFakeBoldText = false
            color = MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorOnSurfaceVariant,
                Color.WHITE
            )
        }

        fun calculateSize() {
            val words = text.text.split(" ")
            val infoWords = text.infoText.split(" ")
            val maxWordWith = max(
                words.maxOf { nameTextPaint.measureText(it) },
                infoWords.maxOf { infoTextPaint.measureText(it) }
            )
            val textHeight =
                nameTextPaint.fontMetrics.run { bottom - top.toInt() }
            val infoTextHeight = infoTextPaint.fontMetrics.run { bottom - top.toInt() }
            val lines = wrapText(text.text, maxWordWith, nameTextPaint)
            val infoLines = wrapText(text.infoText, maxWordWith, infoTextPaint)

            calculatedWidth = max(minCellWidth, maxWordWith + margin * 2)
            calculatedHeight =
                max(
                    minCellHeight,
                    lines.size * textHeight + infoLines.size * infoTextHeight + margin * 2
                )

            updateStaticLayouts()
        }

        fun getCalculatedWidth() = calculatedWidth
        fun getCalculatedHeight() = calculatedHeight

        // Создание прямоуголька и задание радиуса и размера боковой линии
        private var rect = RectF()
        private val margin = 15f

        // пэйнтдля фона
        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.TRANSPARENT
            strokeWidth = 0.5f
            isAntiAlias = true
        }


        private val path = Path()

        private var nameStaticLayout: StaticLayout? = null
        private var infoStaticLayout: StaticLayout? = null

        private fun updateStaticLayouts() {
            nameStaticLayout = getStaticLayout(
                text = text.text,
                width = calculatedWidth - margin * 2,
                paint = nameTextPaint,
                true
            )
            infoStaticLayout = getStaticLayout(
                text = text.infoText,
                width = calculatedWidth - margin * 2,
                paint = infoTextPaint,
                true
            )
        }

        // Начальный Rect для текущих размеров View
        private val untransformedRect = RectF()

        // Если false, таск рисовать не нужно
        val isRectVisible: Boolean
            get() = (rect.top < this@NewView.height) and (rect.bottom >= 0) and (rect.left < this@NewView.width) and (rect.right > 0)


        fun updateInitialRect(x: Int, y: Int) {
            fun getY(): Float {
                return (y + margin) * transformations.scaleFactor + transformations.translationY
            }

            fun getX(): Float {
                return (x + margin) * transformations.scaleFactor + transformations.translationX
            }

            fun getEndX(): Float {
                return (x + calculatedWidth - margin) * transformations.scaleFactor + transformations.translationX
            }

            fun getEndY(): Float {
                return (y + calculatedHeight - margin) * transformations.scaleFactor + transformations.translationY
            }

            untransformedRect.set(
                getX(),
                getY(),
                getEndX(),
                getEndY()
            )

            rect.set(untransformedRect)
        }

        fun draw(canvas: Canvas) {

            path.reset()
            // Отрисовка самого прямоугольника
            path.addRect(rect, Path.Direction.CW)
            canvas.drawPath(path, paint)


            canvas.save()
            canvas.translate(
                rect.left,
                rect.top + (rect.bottom - rect.top - (nameStaticLayout!!.height + infoStaticLayout!!.height) * transformations.scaleFactor) / 2
            )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            nameStaticLayout!!.draw(canvas)
            canvas.restore()

            canvas.save()
            canvas.translate(
                rect.left,
                rect.top + (rect.bottom - rect.top - (infoStaticLayout!!.height - nameStaticLayout!!.height) * transformations.scaleFactor) / 2
            )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            infoStaticLayout!!.draw(canvas)
            canvas.restore()

        }

    }


    private fun calculateTable(
        table: List<CellClass>,
        columnNameText: List<DataForCellClass>,
        rowNameText: List<DataForCellClass>,
        typeInfo: Int,
    ) {

        masOfCellWidth.add(minCellWidth)
        masOfCellHeight.add(minCellHeight)
        columnNameText.forEachIndexed { index, it ->
            val newCell = NamesRect(it)
            newCell.calculateSize()
            columnHeadersStaticLayoutList.add(newCell)
            masOfCellWidth.add(
                max(
                    minCellWidth,
                    newCell.getCalculatedWidth()
                )
            )
            masOfCellHeight[0] = max(newCell.getCalculatedHeight(), minCellHeight)
        }

        rowNameText.forEachIndexed { index, it ->
            val newCell = NamesRect(it)
            newCell.calculateSize()
            rowHeadersStaticLayoutList.add(newCell)
            masOfCellHeight.add(
                max(
                    minCellHeight,
                    newCell.getCalculatedHeight()
                )
            )
            masOfCellWidth[0] = max(newCell.getCalculatedWidth(), minCellWidth)
        }

        var infoText = ""
        table.forEach { cell ->


            infoText = when (typeInfo) {
                CLASSROOM_INFO_TYPE -> cell.teacher + "\n" + cell.classroom
                TEACHER_AND_GROOP_INFO_TYPE -> cell.teacher + "\n" + cell.studyGroup
                else -> {
                    cell.classroom
                }
            }
            val newCell =
                LessonsRect(
                    DataForCellClass(cell.subject, infoText),
                    cell.row,
                    cell.column,
                    cell.color
                )
            newCell.calculateSize()

            lessonsStaticLayoutList.add(newCell)
            masOfCellWidth[cell.column] = max(
                masOfCellWidth[cell.column],
                newCell.getCalculatedWidth()
            )

            if (masOfCellHeight.size > cell.row) {
                masOfCellHeight[cell.row] = max(
                    masOfCellHeight[cell.row],
                    newCell.getCalculatedHeight()
                )
            }

        }
        contentHeight = masOfCellHeight.sum().toInt()
        contentWidth = masOfCellWidth.sum().toInt()

    }

    private fun List<Float>.listSum(index: Int) = this.take(index).sum()


    data class DataForCellClass(
        val text: String,
        val infoText: String,
    )

    private fun clearMas() {
        rowHeadersStaticLayoutList.clear()
        columnHeadersStaticLayoutList.clear()
        lessonsStaticLayoutList.clear()
        masOfCellWidth.clear()
        masOfCellHeight.clear()
        startScaleFactor = null
    }

    companion object {
        const val CLASSROOM_INFO_TYPE = 1
        const val TEACHER_AND_GROOP_INFO_TYPE = 2
    }
}
//**
//тестовые данные
//list = listOf(
//CellClass(
//subject = "Автомобильная подготовка",
//teacher = "Кузнецов Е . В .",
//classroom = "1 / 220",
//studyGroup = "213",
//date = "4 - 03 - 2024",
//subjectType = "6.4 Групповое занятие",
//startTime = "09:00",
//endTime = "10:30",
//subjectNumber = 1,
//noEmpty = true,
//text = "",
//lessonTheme = "",
//color = 2131231033,
//viewSize = 90,
//isGone = true,
//department = "34",
//column = 1,
//row = 1,
//cellType = CellClass.LESSON_CELL_TYPE
//),
//CellClass(
//subject = "Автомобильная подготовка",
//teacher = "Кузнецов Е . В .",
//classroom = "1 / 220",
//studyGroup = "213",
//date = "4 - 03 - 2024",
//subjectType = "6.4 Групповое занятие",
//startTime = "09:00",
//endTime = "10:30",
//subjectNumber = 1,
//noEmpty = true,
//text = "",
//lessonTheme = "",
//color = 2131231033,
//viewSize = 90,
//isGone = true,
//department = "34",
//column = 1,
//row = 2,
//cellType = CellClass.LESSON_CELL_TYPE
//),
//CellClass(
//subject = "Автомобильная подготовка",
//teacher = "Кузнецов Е . В .",
//classroom = "1 / 220",
//studyGroup = "213",
//date = "4 - 03 - 2024",
//subjectType = "6.4 Групповое занятие",
//startTime = "09:00",
//endTime = "10:30",
//subjectNumber = 2,
//noEmpty = true,
//text = "",
//lessonTheme = "",
//color = 2131231033,
//viewSize = 90,
//isGone = true,
//department = "34",
//column = 1,
//row = 3,
//cellType = CellClass.LESSON_CELL_TYPE
//),
//CellClass(
//subject = "Автомобильная подготовкаАвтомобильная подготовкаАвтомобильная подготовка",
//teacher = "Кузнецов Е . В .",
//classroom = "1 / 220",
//studyGroup = "213",
//date = "4 - 03 - 2024",
//subjectType = "6.4 Групповое занятие",
//startTime = "09:00",
//endTime = "10:30",
//subjectNumber = 2,
//noEmpty = true,
//text = "",
//lessonTheme = "",
//color = 2131231033,
//viewSize = 90,
//isGone = true,
//department = "34",
//column = 1,
//row = 4,
//cellType = CellClass.LESSON_CELL_TYPE
//)
//)
//
//val columnNameText = listOf(
//    NewView.DataForCellClass("1000000000000000", " 1 ", 0, 1),
//    NewView.DataForCellClass("2 ", " 2 ", 0, 2),
//    NewView.DataForCellClass("3 ", " 3 ", 0, 3),
//    NewView.DataForCellClass("4 ", " 4 ", 0, 4),
//    NewView.DataForCellClass("5 ", " 5 ", 0, 5),
//    NewView.DataForCellClass("6 ", " 6 ", 0, 6)
//)
//
//val rowNameText = listOf(
//    NewView.DataForCellClass("1000000 0000000 0000000", " 1 ", 1, 0),
//    NewView.DataForCellClass("2 ", " 2 ", 2, 0),
//    NewView.DataForCellClass("3 ", " 3 ", 3, 0),
//    NewView.DataForCellClass("4 ", " 4 ", 4, 0),
//    NewView.DataForCellClass("5 ", " 5 ", 5, 0),
//    NewView.DataForCellClass("6 ", " 6 ", 6, 0)
//)
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
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
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
    private val minCellHeight = 170f
    private val minCellWidth = 200f


    private var startScaleFactor: Float? = null

    private var dateTextSize = 200f

    private var contentWidth: Int = max(width, (minCellWidth * 6 + dateTextSize).toInt())
    private var contentHeight = 500

    // Отвечает за зум и сдвиги
    private var transformations = Transformations()

    // Значения последнего эвента
    private val lastPoint = PointF()
    private var lastPointerId = 0

    private val timeStartOfLessonsList = listOf(
        "9:00", "10:45", "12:30", "14:45", "16:25",
    )
    private val timeEndOfLessonsList = listOf(
        "10:30", "12:15", "14:00", "16:15", "17:55",
    )


    private var currentPeriods = listOf(
        "ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ"
    )


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

    private val mainSeparatorsPaint = Paint().apply {
        strokeWidth = 2f
        color = context.getColor(R.color.gray_400)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            6 * 100
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {


    }

    // Отрисовка просто линии
//    private fun Canvas.drawTimeAndDateLine() {
//        // Линия для отделения времени
//        drawLine(
//            dateTextSize * transformations.scaleFactor + transformations.translationX,
//            0f,
//            dateTextSize * transformations.scaleFactor + transformations.translationX,
//            contentHeight.toFloat() * transformations.scaleFactor + transformations.translationY,
//            mainSeparatorsPaint
//        )
//
//        // Линия для отделения даты
//        drawLine(
//            0f,
//            namesRowHight * transformations.scaleFactor + transformations.translationY,
//            width.toFloat(),
//            namesRowHight * transformations.scaleFactor + transformations.translationY,
//            mainSeparatorsPaint
//        )
//
//        // Нижняя линия конца таблицы
//        drawLine(
//            0f,
//            contentHeight.toFloat() * transformations.scaleFactor + transformations.translationY,
//            width.toFloat(),
//            contentHeight.toFloat() * transformations.scaleFactor + transformations.translationY,
//            mainSeparatorsPaint
//        )
    // }

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

        lessonsStaticLayoutList.forEach {

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

    // Отрисовка линий колонн и названия колонн
//    private fun Canvas.drawPeriods() {
//
//
//    }


    override fun onDraw(canvas: Canvas) = with(canvas) {

        drawTable()
        //drawPeriods()
        //drawTimeAndDateLine()
        invalidate()
    }


    private var timeTable: List<List<CellClass>> = emptyList()


    fun setTimeTable(
        timeTable: List<List<CellClass>> = emptyList(),
        dateList: List<String> = emptyList()
    ) {

        var listOfLists = timeTable.map { listOfCell ->
            listOfCell.filter {
                it.subjectNumber != null
            }
        }
        var list = listOfLists.flatten()


        list = listOf(
            CellClass(
                subject = "Автомобильная подготовка",
                teacher = "Кузнецов Е . В .",
                classroom = "1 / 220",
                studyGroup = "213",
                date = "4 - 03 - 2024",
                subjectType = "6.4 Групповое занятие",
                startTime = "09:00",
                endTime = "10:30",
                subjectNumber = 1,
                noEmpty = true,
                text = "",
                lessonTheme = "",
                color = 2131231033,
                viewSize = 90,
                isGone = true,
                department = "34",
                column = 1,
                row = 1,
                cellType = CellClass.LESSON_CELL_TYPE
            ),
            CellClass(
                subject = "Автомобильная подготовка",
                teacher = "Кузнецов Е . В .",
                classroom = "1 / 220",
                studyGroup = "213",
                date = "4 - 03 - 2024",
                subjectType = "6.4 Групповое занятие",
                startTime = "09:00",
                endTime = "10:30",
                subjectNumber = 1,
                noEmpty = true,
                text = "",
                lessonTheme = "",
                color = 2131231033,
                viewSize = 90,
                isGone = true,
                department = "34",
                column = 1,
                row = 2,
                cellType = CellClass.LESSON_CELL_TYPE
            ),
            CellClass(
                subject = "Автомобильная подготовка",
                teacher = "Кузнецов Е . В .",
                classroom = "1 / 220",
                studyGroup = "213",
                date = "4 - 03 - 2024",
                subjectType = "6.4 Групповое занятие",
                startTime = "09:00",
                endTime = "10:30",
                subjectNumber = 2,
                noEmpty = true,
                text = "",
                lessonTheme = "",
                color = 2131231033,
                viewSize = 90,
                isGone = true,
                department = "34",
                column = 1,
                row = 3,
                cellType = CellClass.LESSON_CELL_TYPE
            ),
            CellClass(
                subject = "Автомобильная подготовка",
                teacher = "Кузнецов Е . В .",
                classroom = "1 / 220",
                studyGroup = "213",
                date = "4 - 03 - 2024",
                subjectType = "6.4 Групповое занятие",
                startTime = "09:00",
                endTime = "10:30",
                subjectNumber = 2,
                noEmpty = true,
                text = "",
                lessonTheme = "",
                color = 2131231033,
                viewSize = 90,
                isGone = true,
                department = "34",
                column = 1,
                row = 4,
                cellType = CellClass.LESSON_CELL_TYPE
            )
        )

        val columnNameText = listOf(
            DataForCellClass(" 1 ", " 1 ", 0, 1),
            DataForCellClass(" 2 ", " 2 ", 0, 2),
            DataForCellClass(" 3 ", " 3 ", 0, 3),
            DataForCellClass(" 4 ", " 4 ", 0, 4),
            DataForCellClass(" 5 ", " 5 ", 0, 5),
            DataForCellClass(" 6 ", " 6 ", 0, 6)
        )

        val rowNameText = listOf(
            DataForCellClass(" 1 ", " 1 ", 1, 0),
            DataForCellClass(" 2 ", " 2 ", 2, 0),
            DataForCellClass(" 3 ", " 3 ", 3, 0),
            DataForCellClass(" 4 ", " 4 ", 4, 0),
            DataForCellClass(" 5 ", " 5 ", 5, 0),
            DataForCellClass(" 6 ", " 6 ", 6, 0)
        )

        calculateTable(list, columnNameText, rowNameText)

    }

    fun setDateTable(list: List<CellClass>, unicList: List<String>) {
        this.currentPeriods = unicList
        contentWidth = (minCellWidth * unicList.size + dateTextSize).toInt()
        transformations.resetTranslation()
        createWorkloadLayoutList(list, unicList)
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


    private fun processMove(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastPoint.set(event.x, event.y)
                lastPointerId = event.getPointerId(0)
                true
            }

            MotionEvent.ACTION_MOVE -> {

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

            else -> false
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
            Log.e("trans", "$translationX   $translationY")
            translationX = (translationX + dx).coerceIn(minTranslationX, 0f)
            translationY = (translationY + dy).coerceIn(minTranslationY, 0f)
        }

        fun resetTranslation() {
            translationX = 0f
            translationY = 0f
        }

        fun addScale(sx: Float) {
            if (sx > 1) {
                if (scaleFactor < maxScaleFactor) {
                    scaleFactor = (scaleFactor * sx).coerceIn(minScaleFactor, maxScaleFactor)
                    translationX = (translationX * sx).coerceIn(minTranslationX, 0f)
                    translationY = (translationY * sx).coerceIn(minTranslationY, 0f)
                }

            } else {
                if (scaleFactor > minScaleFactor && (height < contentHeight * transformations.scaleFactor)) {
                    scaleFactor = (scaleFactor * sx).coerceIn(minScaleFactor, maxScaleFactor)
                    translationX = (translationX * sx).coerceIn(minTranslationX, 0f)
                    translationY = (translationY * sx).coerceIn(minTranslationY, 0f)
                }
            }

        }
    }


    private fun createWorkloadLayoutList(list: List<CellClass>, unicList: List<String>) {

//        // Очищаем списки
//        lineHeadersStaticLayoutList.clear()
//        lessonsStaticLayoutList.clear()
//        lastYList.clear()
//
//        lastY = namesRowHight.toFloat()
//        contentHeight = 0
//
//        repeat(COUNT_OF_LESSONS) { numberOfLesson ->
//            val bufferList = list.filter { lesson ->
//                lesson.subjectNumber == numberOfLesson + 1
//            }.mapNotNull { lesson ->
//                val audIndex = unicList.indexOf(lesson.classroom)
//                if (audIndex >= 0) {
//                    LessonsRect(
//                        text = lesson.subject!!,
//                        infoText = "\n${lesson.studyGroup!!}",
//                        dayOfLesson = audIndex,
//                        lastY = lastY.toInt(),
//                        lastX =
//                        heightOfRow = maxHeightOfRow,
//                        newColor = R.color.blue_fo_lessons_card,
//                        lessonNumber = numberOfLesson
//                    )
//                } else null
//            }
//
//            maxHeightOfRow =
//                max(bufferList.maxOfOrNull { it.height.toInt() } ?: minRowHight, minRowHight)
//
//            val nameOfRowStaticLayout = NamesRect(
//                text = timeStartOfLessonsList[numberOfLesson * 2],
//                infoText = timeStartOfLessonsList[numberOfLesson * 2 + 1],
//                numberOfColumn = ZERO_COLUMN,
//                lastY = lastY.toInt(),
//                heightOfRow = maxHeightOfRow,
//                lessonNumber = numberOfLesson
//            )
//
//            bufferList.forEach { it.setHeightOfRow(maxHeightOfRow) }
//            nameOfRowStaticLayout.setHeightOfRow(maxHeightOfRow)
//
//            lessonsStaticLayoutList.addAll(bufferList)
//            lineHeadersStaticLayoutList.add(nameOfRowStaticLayout)
//
//            lastYList.add(lastY)
//            lastY += maxHeightOfRow
//            maxHeightOfRow = minRowHight
//        }
//
//        contentHeight = lastY.toInt()
    }

    private open inner class CustomRect
    private inner class LessonsRect(
        text: String,
        infoText: String,
        private val dayOfLesson: Int,
        private var lastY: Int,
        private var lastX: Int,
        private var heightOfRow: Int,
        private val newColor: Int,
        val lessonNumber: Int,
        val length: Int = 500
    ) : CustomRect() {

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
            textSize = resources.getDimension(R.dimen.lesson_name_text_size)
            isFakeBoldText = false
            color = MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorOnSurfaceVariant,
                Color.WHITE
            )
        }

        // Создание прямоуголька и задание радиуса и размера боковой линии
        private var rect = RectF()
        private var rectRadius = 10f * transformations.scaleFactor
        private var verticalLineSize = 10f * transformations.scaleFactor
        private val rectStrokeWidth = 2f

        // пэйнт для линии и для фона
        private val paint = Paint().apply {
            style = Paint.Style.FILL
            strokeWidth = 0.5f
            isAntiAlias = true
        }

        @SuppressLint("ResourceType")
        private val strokePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = rectStrokeWidth
            color = resources.getColor(newColor, null)
        }

        private val paddingX = 10f
        private val paddingY = 10f
        private val margin = 15f
        private val path = Path()
        private val path2 = Path()

        // Начальный Rect для текущих размеров View
        private val untransformedRect = RectF()

        // Если false, таск рисовать не нужно
        val isRectVisible: Boolean
            get() = ((rect.top < height) or (rect.bottom > 0)) and ((rect.right < width) or (rect.left > 0))

        private val staticLayoutWidth =
            (minCellWidth * transformations.scaleFactor - 2 * margin - 2 * paddingX - verticalLineSize - rectStrokeWidth) / transformations.scaleFactor

        // Создаем статик лэйаут
        private val nameStaticLayout = getStaticLayout(
            text,
            staticLayoutWidth,
            nameTextPaint,
            true
        )

        private val infoStaticLayout = getStaticLayout(
            infoText,
            staticLayoutWidth,
            infoTextPaint,
            true
        )

        val height: Float
            get() = nameStaticLayout.height + infoStaticLayout.height + paddingY + paddingY + margin + margin

        val width: Float
            get() = nameStaticLayout.width.toFloat()


        fun updateInitialRect() {


            fun getX(index: Int): Float {
                return ((index * minCellWidth) + dateTextSize) * transformations.scaleFactor + transformations.translationX + margin
            }

            fun getEndX(index: Int): Float {
                return (((index + 1) * minCellWidth) + dateTextSize) * transformations.scaleFactor + transformations.translationX - margin
            }

            fun getY(): Float {
                return (lastY.toFloat() + (heightOfRow - height + margin + margin) / 2) * transformations.scaleFactor + transformations.translationY
            }

            // Создание самой формы прямоугольника
            untransformedRect.set(
                getX(dayOfLesson),
                getY(),
                getEndX(dayOfLesson),
                getY() + (height - 2 * margin) * transformations.scaleFactor,
            )
            rect.set(untransformedRect)
        }

        fun draw(canvas: Canvas) {


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

            canvas.drawRoundRect(strokeRect, rectRadius, verticalLineSize, strokePaint)

            canvas.save()
            canvas.translate(
                rect.left + verticalLineSize + paddingX + rectStrokeWidth,
                rect.top + (rect.bottom - rect.top - (nameStaticLayout.height + infoStaticLayout.height) * transformations.scaleFactor) / 2
            )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            nameStaticLayout.draw(canvas)
            canvas.restore()

            canvas.save()
            canvas.translate(
                rect.left + verticalLineSize + paddingX + rectStrokeWidth,
                rect.top + (rect.bottom - rect.top - (infoStaticLayout.height - nameStaticLayout.height) * transformations.scaleFactor) / 2
            )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            infoStaticLayout.draw(canvas)
            canvas.restore()

        }

        fun setHeightOfRow(newHeight: Int) {
            heightOfRow = newHeight
        }


        private fun changeColor(newColor: Int): Int {
            return when (newColor) {
                R.color.red_fo_lessons_card -> R.color.red_fo_lessons_card_alpha
                R.color.yellow_fo_lessons_card -> R.color.yellow_fo_lessons_card_alpha
                R.color.green_fo_lessons_card -> R.color.green_fo_lessons_card_alpha
                R.color.blue_fo_lessons_card -> R.color.blue_fo_lessons_card_alpha
                else -> {
                    R.color.white
                }
            }

        }

        fun setLastY(newY: Int) {
            lastY = newY
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
            textSize = resources.getDimension(R.dimen.lesson_name_text_size)
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

            calculatedWidth = max(minCellWidth, maxWordWith)
            calculatedHeight =
                max(minCellHeight, lines.size * textHeight + infoLines.size * infoTextHeight)
        }

        fun getCalculatedWidth() = calculatedWidth
        fun getCalculatedHeight() = calculatedHeight

        // Создание прямоуголька и задание радиуса и размера боковой линии
        private var rect = RectF()

        // пэйнтдля фона
        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.TRANSPARENT
            strokeWidth = 0.5f
            isAntiAlias = true
        }

        // Создаем статик лэйаут
        private val nameStaticLayout = getStaticLayout(
            text.text,
            calculatedWidth,
            nameTextPaint,
            true
        )

        private val infoStaticLayout = getStaticLayout(
            text.infoText,
            calculatedWidth,
            infoTextPaint,
            true
        )

        private val margin = 15f
        private val path = Path()

        // Начальный Rect для текущих размеров View
        private val untransformedRect = RectF()

        // Если false, таск рисовать не нужно
        val isRectVisible: Boolean
            get() = (rect.top < this@NewView.height) and (rect.bottom >= 0) and (rect.left < this@NewView.width) and (rect.right > 0)


        val height: Float
            get() = nameStaticLayout.height + infoStaticLayout.height + margin + margin

        val width: Float
            get() = max(nameStaticLayout.width, infoStaticLayout.width) + margin + margin


        fun updateInitialRect(x: Int, y: Int) {
            fun getY(): Float {
                return (y) * transformations.scaleFactor + transformations.translationY
            }

            fun getX(): Float {
                return (x) * transformations.scaleFactor + transformations.translationX
            }

            fun getEndX(): Float {
                return (x + calculatedWidth + 2 * margin) * transformations.scaleFactor + transformations.translationX
            }

            fun getEndY(): Float {
                return (y + calculatedHeight + 2 * margin) * transformations.scaleFactor + transformations.translationY
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


            // Отрисовка самого прямоугольника
            path.addRect(rect, Path.Direction.CW)
            canvas.drawPath(path, paint)


            canvas.save()
            canvas.translate(
                rect.left + margin,
                rect.top + (rect.bottom - rect.top - (nameStaticLayout.height + infoStaticLayout.height) * transformations.scaleFactor) / 2
            )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            nameStaticLayout.draw(canvas)
            canvas.restore()

            canvas.save()
            canvas.translate(
                rect.left + margin,
                rect.top + (rect.bottom - rect.top - (infoStaticLayout.height - nameStaticLayout.height) * transformations.scaleFactor) / 2
            )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            infoStaticLayout.draw(canvas)
            canvas.restore()

        }

    }

    // Переменные для отрисовки строк

    private var rowHeadersStaticLayoutList = mutableListOf<CustomRect>()
    private var columnHeadersStaticLayoutList = mutableListOf<CustomRect>()
    private var lessonsStaticLayoutList = mutableListOf<CustomRect>()

    // В эти перемемнные сохраняются ширина и высота ячеек,
    // т.к. они могут меняться из-за изменяемоего контента

    private val masOfCellWidth = mutableListOf<Float>()
    private val masOfCellHeight = mutableListOf<Float>()


    private fun calculateTable(
        table: List<CellClass>,
        columnNameText: List<DataForCellClass>,
        rowNameText: List<DataForCellClass>
    ) {

        masOfCellWidth.add(minCellWidth)
        masOfCellHeight.add(minCellHeight)
        columnNameText.forEach {
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

        rowNameText.forEach {
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


        table.forEach { cell ->
            val newCell =
                NamesRect(DataForCellClass(cell.text, cell.classroom, cell.row, cell.column))
            newCell.calculateSize()
            lessonsStaticLayoutList.add(newCell)
            masOfCellWidth[cell.column] = max(
                masOfCellWidth[cell.column],
                newCell.getCalculatedWidth()
            )
            masOfCellHeight[cell.row] = max(
                masOfCellHeight[cell.row],
                newCell.getCalculatedHeight()
            )

        }
        contentHeight = masOfCellHeight.sum().toInt()
        Log.e("table", masOfCellHeight.toString() + " " + masOfCellWidth.toString())
    }

    private fun List<Float>.listSum(index: Int) = this.take(index).sum()


    companion object {
        const val COUNT_OF_LESSONS = 5
    }

    data class DataForCellClass(
        val text: String,
        val infoText: String,
        val row: Int,
        val column: Int,
    )

}

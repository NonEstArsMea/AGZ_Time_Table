package com.NonEstArsMea.agz_time_table.present.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.ContextCompat
import com.NonEstArsMea.agz_time_table.R
import com.NonEstArsMea.agz_time_table.domain.dataClass.CellClass
import com.NonEstArsMea.agz_time_table.util.getStaticLayout
import java.lang.Integer.max

class NewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Основная информация об строках и колонках
    private val minRowHight = 170
    private val namesRowHight = 170
    private val columnWidth = 200f

    private var startScaleFactor: Float? = null

    private var dateTextSize = 200f

    private val contentWidth: Int
        get() = max(width, (columnWidth * 6 + dateTextSize).toInt())
    private var contentHeight = 500

    // Отвечает за зум и сдвиги
    private val transformations = Transformations()

    // Значения последнего эвента
    private val lastPoint = PointF()
    private var lastPointerId = 0

    private val timeStartOfLessonsList = listOf(
        "9:00", "10:30",
        "10:45", "12:15",
        "12:30", "14:00",
        "14:15", "16:15",
        "16:25", "17:55",
    )

    private var dateList = listOf<String>()


    private val rowPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
    }

    private val separatorsPaint = Paint().apply {
        strokeWidth = 2f
        color = context.getColor(R.color.gray_400)
    }

    private val mainSeparatorsPaint = Paint().apply {
        strokeWidth = 2f
        color = context.getColor(R.color.gray_400)
    }

    private val dateNamePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = resources.getDimension(R.dimen.name_text_size)
        isFakeBoldText = true
        color = ContextCompat.getColor(context, R.color.black)
    }

    // Rect для рисования строк
    private val rowRect = Rect()


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
        // Размер изменился, надо пересчитать ширину строки
        rowRect.set(
            /* left = */ 0,
            /* top = */0,
            /* right = */w,
            /* bottom = */ minRowHight
        )

    }

    // Отрисовка просто линии
    private fun Canvas.drawTimeAndDateLine() {
        // Линия для отделения времени
        drawLine(
            dateTextSize * transformations.scaleFactor + transformations.translationX,
            0f,
            dateTextSize * transformations.scaleFactor + transformations.translationX,
            contentHeight.toFloat() + transformations.translationY,
            mainSeparatorsPaint
        )

        // Линия для отделения даты
        drawLine(
            0f,
            namesRowHight * transformations.scaleFactor + transformations.translationY,
            width.toFloat(),
            namesRowHight * transformations.scaleFactor + transformations.translationY,
            mainSeparatorsPaint
        )

        // Нижняя линия конца таблицы
        drawLine(
            0f,
            contentHeight.toFloat() + transformations.translationY,
            width.toFloat(),
            contentHeight.toFloat() + transformations.translationY,
            mainSeparatorsPaint
        )
    }

    // Отрисовка конкретных данных в строках
    private fun Canvas.drawRowsAndDates() {


        var lastY = namesRowHight.toFloat() * transformations.scaleFactor

        repeat(COUNT_OF_LESSONS) { numberOfLesson ->
            var maxHeightOfRow = minRowHight

            val nameOfROwStaticLayout = NamesRect(
                text = timeStartOfLessonsList[numberOfLesson * 2],
                infoText = timeStartOfLessonsList[numberOfLesson * 2 + 1],
                ZERO_COLUMN,
                lastY.toInt(),
                minRowHight
            )

            // Получаем ячейки с парами и измеряем их
            // Получаем элемент массива, в нашем случае это расписание на день
            for (day in timeTable.indices) {
                // Если не пустой
                if (timeTable[day].isNotEmpty()) {
                    for (a in timeTable[day]) {
                        if (a.subjectNumber == numberOfLesson + 1) {
                            val lesson = LessonsRect(
                                text = a.subject!!,
                                infoText = "\n${a.classroom!!}",
                                dayOfLesson = day,
                                lastY = lastY.toInt(),
                                heightOfRow = maxHeightOfRow,
                                newColor = a.color
                            )
                            maxHeightOfRow = max(lesson.height.toInt(), maxHeightOfRow)
                        }
                    }

                }
            }

            // Разбираемся с высотой
            maxHeightOfRow = max(nameOfROwStaticLayout.height.toInt(), maxHeightOfRow)

            // Отрисовка строки, можно изменять фон
            rowRect.set(
                /* left = */ 0,
                /* top = */
                (lastY + transformations.translationY).toInt(),
                /* right = */
                width,
                /* bottom = */
                (lastY + (maxHeightOfRow * transformations.scaleFactor) + transformations.translationY).toInt()
            )
            drawRect(rowRect, rowPaint)


            // Получаем ячейки с парами и измеряем их
            // Получаем элемент массива, в нашем случае это расписание на день
            for (day in timeTable.indices) {
                // Если не пустой
                if (timeTable[day].isNotEmpty()) {
                    for (a in timeTable[day]) {
                        if (a.subjectNumber == numberOfLesson + 1) {
                            val lesson = LessonsRect(
                                text = a.subject!!,
                                infoText = "\n${a.classroom!!}",
                                dayOfLesson = day,
                                lastY = lastY.toInt(),
                                heightOfRow = maxHeightOfRow,
                                newColor = a.color
                            )
                            if (lesson.isRectVisible) {
                                lesson.updateInitialRect()
                                lesson.draw(this)
                            }
                        }
                    }

                }
            }


            if (nameOfROwStaticLayout.isRectVisible) {
                nameOfROwStaticLayout.updateInitialRect()
                nameOfROwStaticLayout.draw(this)
            }

            lastY += (maxHeightOfRow * transformations.scaleFactor)

            // Разделитель
            drawLine(
                /* startX = */ 0f,
                /* startY = */ (lastY + transformations.translationY),
                /* stopX = */ width.toFloat(),
                /* stopY = */ (lastY + transformations.translationY),
                /* paint = */ separatorsPaint
            )

        }
        contentHeight = lastY.toInt()

        //Проверка на выполнение один раз

        if (startScaleFactor == null) {
            startScaleFactor = height.toFloat() / contentHeight.toFloat()
            transformations.scaleFactor = startScaleFactor!!
            transformations.minScaleFactor = startScaleFactor!!
            invalidate()
        }

    }

    // Отрисовка линий колонн и названия колонн
    private fun Canvas.drawPeriods() {
        val currentPeriods = listOf(
            "ПН",
            "ВТ",
            "СР",
            "ЧТ",
            "ПТ",
            "СБ"
        )

        var lastX = dateTextSize * transformations.scaleFactor + transformations.translationX


        currentPeriods.forEachIndexed { index, periodName ->
            // По X текст рисуется относительно его начала
            val periodName = NamesRect(
                text = periodName,
                infoText = dateList[index],
                numberOfColumn = index,
                lastY = 0,
                heightOfRow = namesRowHight
            )

            if (periodName.isRectVisible) {
                periodName.updateInitialRect()
                periodName.draw(this)
            }


            lastX += (columnWidth * transformations.scaleFactor)


            // Разделитель
            drawLine(
                /* startX = */ lastX,
                /* startY = */ 0f,
                /* stopX = */ lastX,
                /* stopY = */ contentHeight.toFloat() + transformations.translationY,
                /* paint = */ separatorsPaint
            )
        }
    }

    private fun Paint.getTextBaselineByCenter() = (descent() + ascent()) / 2

    override fun onDraw(canvas: Canvas) = with(canvas) {
        drawRowsAndDates()
        drawPeriods()
        drawTimeAndDateLine()
    }


    private var timeTable: List<List<CellClass>> = emptyList()


    fun setTimeTable(timeTable: List<List<CellClass>>, dateList: List<String>) {
        Log.e("table", timeTable.toString())
        Log.e("table", this.timeTable.toString())

        val list = timeTable.map { listOfCell ->
            listOfCell.filter {
                it.subjectNumber != null
            }
        }


        this.timeTable = list
        this.dateList = dateList
        requestLayout()
        invalidate()

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
                if (width < contentWidth) {
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
            get() = (width - contentWidth * scaleFactor).coerceAtMost(0f).toFloat()
        private val minTranslationY: Float
            get() = (height - contentHeight).coerceAtMost(0).toFloat()

        fun addTranslation(dx: Float, dy: Float) {
            translationX = (translationX + dx).coerceIn(minTranslationX, 0f)
            translationY = (translationY + dy).coerceIn(minTranslationY, 0f)
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
                if (scaleFactor > minScaleFactor && (height < contentHeight)) {
                    scaleFactor = (scaleFactor * sx).coerceIn(minScaleFactor, maxScaleFactor)
                    translationX = (translationX * sx).coerceIn(minTranslationX, 0f)
                    translationY = (translationY * sx).coerceIn(minTranslationY, 0f)
                    invalidate()
                }
            }

        }
    }

    private inner class LessonsRect(
        private val text: String,
        private val infoText: String,
        private val dayOfLesson: Int,
        private val lastY: Int,
        private var heightOfRow: Int,
        private val newColor: Int,
    ) {

        private val nameTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = resources.getDimension(R.dimen.lesson_name_text_size)
            isFakeBoldText = true
            color = ContextCompat.getColor(context, R.color.black)
        }

        private val infoTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = resources.getDimension(R.dimen.lesson_name_text_size)
            isFakeBoldText = false
            color = ContextCompat.getColor(context, R.color.grey_500)
        }

        // Создание прямоуголька и задание радиуса и размера боковой линии
        private var rect = RectF()
        private var rectRadius = 10f * transformations.scaleFactor
        private var verticalLineSize = 10f * transformations.scaleFactor
        private val rectStrokeWidth = 2f

        // пэйнт для линии и для фона
        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
            strokeWidth = 0.5f
            isAntiAlias = true
            setBackgroundColor(Color.WHITE)
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
            (columnWidth * transformations.scaleFactor - 2 * margin - 2 * paddingX - verticalLineSize - rectStrokeWidth) / transformations.scaleFactor

        // Создаем статик лэйаут
        private val nameStaticLayout = getStaticLayout(
            text,
            staticLayoutWidth.toInt(),
            nameTextPaint,
            true
        )

        private val infoStaticLayout = getStaticLayout(
                infoText,
                staticLayoutWidth.toInt(),
                infoTextPaint,
                true
            )

        val height: Float
            get() = nameStaticLayout.height + infoStaticLayout.height + paddingY + paddingY + margin + margin


        fun updateInitialRect() {


            fun getX(index: Int): Float {
                return ((index * columnWidth) + dateTextSize) * transformations.scaleFactor + transformations.translationX + margin
            }

            fun getEndX(index: Int): Float {
                return (((index + 1) * columnWidth) + dateTextSize) * transformations.scaleFactor + transformations.translationX - margin
            }

            fun getY(): Float {
                return lastY.toFloat() + (heightOfRow - height + margin + margin) * transformations.scaleFactor / 2 + transformations.translationY
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


        private fun changeColor(newColor: Int): Int {
            return when (newColor) {
                R.color.red_fo_lessons_card -> R.color.red_fo_lessons_card_alpha
                R.color.yellow_fo_lessons_card -> R.color.yellow_fo_lessons_card_alpha
                R.color.green_fo_lessons_card -> R.color.green_fo_lessons_card_alpha
                else -> {
                    R.color.white
                }
            }

        }

    }

        private inner class NamesRect(
            private val text: String,
            private val infoText: String,
            private val numberOfColumn: Int,
            private val lastY: Int,
            private var heightOfRow: Int,
        ) {

            private val nameTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = resources.getDimension(R.dimen.name_text_size)
                isFakeBoldText = true
                color = ContextCompat.getColor(context, R.color.black)
            }

            private val infoTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = resources.getDimension(R.dimen.info_text_size)
                isFakeBoldText = false
                color = ContextCompat.getColor(context, R.color.grey_500)
            }

            // Создание прямоуголька и задание радиуса и размера боковой линии
            private var rect = RectF()

            // пэйнт для линии и для фона
            private val paint = Paint().apply {
                style = Paint.Style.FILL
                color = Color.WHITE
                strokeWidth = 0.5f
                isAntiAlias = true
                setBackgroundColor(Color.WHITE)
            }

            private val margin = 15f
            private val path = Path()

            // Начальный Rect для текущих размеров View
            private val untransformedRect = RectF()

            // Если false, таск рисовать не нужно
            val isRectVisible: Boolean
                get() = ((rect.top < height) or (rect.bottom > 0)) and ((rect.right < width) or (rect.left > 0))

            private val staticLayoutWidth =
                (columnWidth * transformations.scaleFactor - 2 * margin) / transformations.scaleFactor

            // Создаем статик лэйаут
            private val nameStaticLayout = getStaticLayout(
                text,
                staticLayoutWidth.toInt(),
                nameTextPaint,
                true
            )

            private val infoStaticLayout = getStaticLayout(
                infoText,
                staticLayoutWidth.toInt(),
                infoTextPaint,
                true
            )

            val height: Float
                get() = nameStaticLayout.height + infoStaticLayout.height + margin + margin


            fun updateInitialRect() {
                fun getY(): Float {
                    return lastY.toFloat() + (heightOfRow - height + margin + margin) * transformations.scaleFactor / 2 + transformations.translationY
                }

                fun getX(index: Int): Float {
                    return ((index * columnWidth) + dateTextSize) * transformations.scaleFactor + transformations.translationX + margin
                }

                fun getEndX(index: Int): Float {
                    return (((index + 1) * columnWidth) + dateTextSize) * transformations.scaleFactor + transformations.translationX - margin
                }

                // Создание самой формы прямоугольника
                if(numberOfColumn == ZERO_COLUMN){
                    untransformedRect.set(
                        0  + transformations.translationX + margin,
                        getY(),
                        dateTextSize * transformations.scaleFactor + transformations.translationX - margin,
                        getY() + (height - 2 * margin) * transformations.scaleFactor,
                    )
                }else{
                    untransformedRect.set(
                        getX(numberOfColumn),
                        getY(),
                        getEndX(numberOfColumn),
                        getY() + (height - 2 * margin) * transformations.scaleFactor,
                    )
                }

                rect.set(untransformedRect)
            }

            fun draw(canvas: Canvas) {


                // Отрисовка самого прямоугольника
                path.addRect(rect, Path.Direction.CW)
                canvas.drawPath(path, paint)


                canvas.save()
                canvas.translate(
                    rect.left,
                    rect.top + (rect.bottom - rect.top - (nameStaticLayout.height + infoStaticLayout.height) * transformations.scaleFactor) / 2
                )
                canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
                nameStaticLayout.draw(canvas)
                canvas.restore()

                canvas.save()
                canvas.translate(
                    rect.left,
                    rect.top + (rect.bottom - rect.top - (infoStaticLayout.height - nameStaticLayout.height) * transformations.scaleFactor) / 2
                )
                canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
                infoStaticLayout.draw(canvas)
                canvas.restore()

            }

        }


    companion object {
        const val COUNT_OF_LESSONS = 5
        const val ZERO_COLUMN = -1
    }

}

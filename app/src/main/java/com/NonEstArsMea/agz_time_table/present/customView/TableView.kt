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
import java.lang.Integer.max

class NewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Основная информация об строках и колонках
    private val minRowHight = 170
    private val namesRowHight = 170
    private val columnWidth = 350f

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


    private val rowPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.TRANSPARENT
    }

    private val separatorsPaint = Paint().apply {
        strokeWidth = 2f
        color = Color.GRAY
    }

    private val mainSeparatorsPaint = Paint().apply {
        strokeWidth = 2f
        color = context.getColor(R.color.gray_400)
    }


    private val dateNamePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = resources.getDimension(R.dimen.gant_period_name_text_size)
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


    private fun Canvas.drawRowsAndDates() {


        var lastY = namesRowHight.toFloat() * transformations.scaleFactor
        val lastX = transformations.translationX

        val paddingLeftAndRight = 5
        var textY: Float
        var textX: Float


        val texts = listOf(
            "1 Пара",
            "2 Пара",
            "3 Пара",
            "4 Пара",
            "5 Пара",
        )

        repeat(COUNT_OF_LESSONS) { index ->
            var maxHeightOfRow = minRowHight

            val nameOfROwStaticLayout =
                getStaticLayout(
                    texts[index],
                    dateTextSize.toInt() - 2 * paddingLeftAndRight,
                    dateNamePaint
                )
            // Получаем ячейки с парами и измеряем их

            (0..4).forEach { numberOfLesson ->
                for (day in timeTable) {
                    if (day.size - 1 >= numberOfLesson) {
                        Log.e("lessons", "${day.size}   $numberOfLesson")
                        val lesson = LessonsRect(
                            text = day[numberOfLesson].subject!!,
                            dayOfLesson = numberOfLesson + 1,
                            subjectNumber = day[numberOfLesson].subjectNumber!!,
                            lastY = lastY.toInt(),
                            hightOfRow = maxHeightOfRow
                        )
                        maxHeightOfRow = max(lesson.height.toInt(), maxHeightOfRow)
                    }
                }
            }


            Log.e("flag", "------------------------------- $timeTable")

            for (day in timeTable.indices) {
                for (lessonOfDay in timeTable[day].indices) {
                    timeTable[day][lessonOfDay].subjectNumber?.let {
                        val lesson = LessonsRect(
                            text = timeTable[day][lessonOfDay].subject!!,
                            dayOfLesson = day,
                            subjectNumber = timeTable[day][lessonOfDay].subjectNumber!!,
                            lastY = lastY.toInt(),
                            hightOfRow = maxHeightOfRow
                        )
                        maxHeightOfRow = max(lesson.height.toInt(), maxHeightOfRow)
                    }
                }
            }
            // Разбираемся с высотой
            maxHeightOfRow = max(nameOfROwStaticLayout.height, maxHeightOfRow)


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
            (0..4).forEach { numberOfLesson ->
                for (day in timeTable) {
                    if (day.size - 1 >= numberOfLesson) {
                        val lesson = LessonsRect(
                            text = day[numberOfLesson].subject!!,
                            dayOfLesson = numberOfLesson + 1,
                            subjectNumber = day[numberOfLesson].subjectNumber!!,
                            lastY = lastY.toInt(),
                            hightOfRow = maxHeightOfRow
                        )
                        if (lesson.isRectVisible) {
                            lesson.updateInitialRect()
                            lesson.draw(this)
                        }
                    }
                }
            }




            textY =
                (lastY + (maxHeightOfRow - nameOfROwStaticLayout.height) * transformations.scaleFactor / 2) + transformations.translationY
            textX = paddingLeftAndRight.toFloat() + lastX

            this.save()
            this.translate(textX, textY)
            this.scale(transformations.scaleFactor, transformations.scaleFactor)
            nameOfROwStaticLayout.draw(this)
            this.restore()

            lastY += (maxHeightOfRow * transformations.scaleFactor)

            // Разделитель
            drawLine(
                /* startX = */ 0f,
                /* startY = */ (lastY  + transformations.translationY - 5f),
                /* stopX = */ width.toFloat(),
                /* stopY = */ (lastY  + transformations.translationY - 5f),
                /* paint = */ separatorsPaint
            )

        }
        contentHeight = lastY.toInt()

    }


    private fun Canvas.drawPeriods() {
        val currentPeriods = listOf(
            "17\n" + "ПН",
            "18\n" + "ВТ",
            "19\n" + "СР",
            "20\n" + "ЧТ",
            "21\n" + "ПТ",
            "22\n" + "СБ"
        )
        var textY: Float
        var textX: Float

        var lastX = dateTextSize * transformations.scaleFactor + transformations.translationX

        val layoutColumnWidth = columnWidth * transformations.scaleFactor

        currentPeriods.forEachIndexed { index, periodName ->
            // По X текст рисуется относительно его начала
            val staticLayout = StaticLayout.Builder.obtain(
                /* source = */ periodName,
                /* start = */0,
                /* end = */periodName.length,
                /* paint = */dateNamePaint,
                /* width = */layoutColumnWidth.toInt()
            )
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setLineSpacing(0f, 1f)
                .setIncludePad(true)
                .build()

            textY =
                ((namesRowHight - staticLayout.height) / 2 * transformations.scaleFactor) + transformations.translationY
            textX = lastX + (columnWidth - staticLayout.width) / 2 * transformations.scaleFactor

            this.save()
            this.translate(textX, textY)
            this.scale(transformations.scaleFactor, transformations.scaleFactor)
            staticLayout.draw(this)
            this.restore()

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

        setTimeTable(
            listOf(
                listOf(
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
                        text = null,
                        lessonTheme = null,
                        color = 2131231033,
                        viewType = null,
                        viewSize = null,
                        isGone = true,
                        department = "34"
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
                        text = null,
                        lessonTheme = null,
                        color = 2131231033,
                        viewType = null,
                        viewSize = null,
                        isGone = true,
                        department = "34"
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
                        text = null,
                        lessonTheme = null,
                        color = 2131231033,
                        viewType = null,
                        viewSize = null,
                        isGone = true,
                        department = "34"
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
                        text = null,
                        lessonTheme = null,
                        color = 2131231033,
                        viewType = null,
                        viewSize = null,
                        isGone = true,
                        department = "34"
                    )
                )
            )
        )
    }


    private var timeTable: List<List<CellClass>> = emptyList()


    fun setTimeTable(timeTable: List<List<CellClass>>) {
        if (timeTable != this.timeTable) {
            val list = timeTable.map { listOfCell ->
                listOfCell.filter {
                    it.subjectNumber != null
                }
            }
            this.timeTable = list
            requestLayout()
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


    companion object {
        const val COUNT_OF_LESSONS = 5
    }

    private inner class Transformations {

        var scaleFactor = 1.0f
        private var minScaleFactor = 0.1f
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
        val text: String,
        val dayOfLesson: Int,
        val lastY: Int,
        val subjectNumber: Int,
        var hightOfRow: Int
    ) {

        var rect = RectF()

        var rectRadius = 20f * transformations.scaleFactor
        private var verticalLineSize = 15f * transformations.scaleFactor


        private val paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            strokeWidth = 1f
            setBackgroundColor(Color.WHITE)
        }

        private val strokePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = strokeWidth
            color = Color.BLACK
        }

        private val strokeWidth = 1f
        val paddingX = 10f
        val paddingY = 10f
        val margin = 15f
        private val path = Path()
        private val path2 = Path()

        // Начальный Rect для текущих размеров View
        private val untransformedRect = RectF()

        // Если false, таск рисовать не нужно
        val isRectVisible: Boolean
            get() = ((rect.top < height) or (rect.bottom > 0)) and ((rect.right < width) or (rect.left > 0))

        private val staticLayout = getStaticLayout(
            text,
            columnWidth.toInt() - paddingX.toInt() - paddingX.toInt()- margin.toInt() - margin.toInt(),
            dateNamePaint,
            true
        )
        val height = staticLayout.height + paddingY + paddingY + margin + margin

        fun updateInitialRect() {


            fun getX(index: Int): Float {
                return (index * columnWidth) + dateTextSize
            }

            fun getEndX(index: Int): Float {
                return ((index + 1) * columnWidth) + dateTextSize
            }

            untransformedRect.set(
                getX(dayOfLesson) * transformations.scaleFactor + transformations.translationX + margin,
                lastY.toFloat() + transformations.translationY + margin,
                getEndX(dayOfLesson) * transformations.scaleFactor + transformations.translationX - margin,
                (lastY + height * transformations.scaleFactor) + transformations.translationY - margin - paddingY,
            )
            rect.set(untransformedRect)
        }

        fun draw(canvas: Canvas) {
            // Draw rounded rectangle

            paint.color = Color.WHITE
            val strokeRect = RectF(
                rect.left - strokeWidth,
                rect.top - strokeWidth,
                rect.right + strokeWidth,
                rect.bottom + strokeWidth
            )
            // Отрисовка самого прямоугольника
            path.addRoundRect(rect, rectRadius, rectRadius, Path.Direction.CW)
            canvas.drawPath(path, paint)

            paint.color = resources.getColor(R.color.gray_light)
            // Отрисовка боковой части
            with(path) {
                reset()
                addRoundRect(
                    rect.left,
                    rect.top,
                    rect.left + 100f,
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
            canvas.translate(rect.left + verticalLineSize + paddingX, rect.top + (rect.bottom - rect.top - staticLayout.height) / 2 )
            canvas.scale(transformations.scaleFactor, transformations.scaleFactor)
            staticLayout.draw(canvas)
            canvas.restore()
        }

    }

    fun getStaticLayout(
        text: String,
        width: Int,
        paint: TextPaint,
        alignLeft: Boolean = false
    ): StaticLayout {
        val align = if (alignLeft) {
            Layout.Alignment.ALIGN_NORMAL
        } else {
            Layout.Alignment.ALIGN_CENTER
        }
        return StaticLayout.Builder.obtain(
            /* source = */ text,
            /* start = */  0,
            /* end = */    text.length,
            /* paint = */  paint,
            /* width = */  width - 10
        )
            .setAlignment(align)
            .setLineSpacing(0f, 1f)
            .setIncludePad(true)
            .build()
    }
}

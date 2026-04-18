package com.github.terrakok.oefoef.ui.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Icons {
    val Back by lazy {
        ImageVector
            .Builder(
                name = "arrow_back",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Companion.Transparent),
                ) {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(24f)
                    verticalLineToRelative(24f)
                    horizontalLineTo(0f)
                    verticalLineTo(0f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Companion.Black),
                ) {
                    moveTo(19f, 11f)
                    horizontalLineTo(7.83f)
                    lineToRelative(4.88f, -4.88f)
                    curveToRelative(0.39f, -0.39f, 0.39f, -1.03f, 0f, -1.42f)
                    curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0f)
                    lineToRelative(-6.59f, 6.59f)
                    curveToRelative(-0.39f, 0.39f, -0.39f, 1.02f, 0f, 1.41f)
                    lineToRelative(6.59f, 6.59f)
                    curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.41f, 0f)
                    curveToRelative(0.39f, -0.39f, 0.39f, -1.02f, 0f, -1.41f)
                    lineTo(7.83f, 13f)
                    horizontalLineTo(19f)
                    curveToRelative(0.55f, 0f, 1f, -0.45f, 1f, -1f)
                    reflectiveCurveToRelative(-0.45f, -1f, -1f, -1f)
                    close()
                }
            }.build()
    }

    val Play by lazy {
        ImageVector
            .Builder(
                name = "play",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 448f,
                viewportHeight = 512f,
            ).apply {
                path(
                    fill = SolidColor(Color.Companion.Black),
                ) {
                    moveTo(424.4f, 214.7f)
                    lineTo(72.4f, 6.6f)
                    curveTo(43.8f, -10.3f, 0f, 6.1f, 0f, 47.9f)
                    verticalLineTo(464f)
                    curveToRelative(0f, 37.5f, 40.7f, 60.1f, 72.4f, 41.3f)
                    lineToRelative(352f, -208f)
                    curveToRelative(31.4f, -18.5f, 31.5f, -64.1f, 0f, -82.6f)
                    close()
                }
            }.build()
    }

    val BrainGym by lazy {
        ImageVector.Builder(
            name = "brain-sparkle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF212121))
            ) {
                moveTo(9.5f, 2f)
                curveTo(10.6822f, 2f, 11.7164f, 2.63146f, 12.2852f, 3.5752f)
                curveTo(12.1045f, 3.82998f, 12.0009f, 4.13905f, 12f, 4.46875f)
                curveTo(12.0017f, 5.00848f, 12.3016f, 5.55275f, 12.75f, 5.85938f)
                verticalLineTo(18.25f)
                curveTo(12.75f, 19.4909f, 13.7615f, 20.5f, 15.0059f, 20.5f)
                curveTo(16.1641f, 20.4998f, 17.1471f, 19.5956f, 17.2529f, 18.4316f)
                curveTo(17.2883f, 18.0455f, 17.6122f, 17.75f, 18f, 17.75f)
                curveTo(19.3807f, 17.75f, 20.5f, 16.6307f, 20.5f, 15.25f)
                verticalLineTo(14.25f)
                curveTo(20.5f, 13.9694f, 20.4643f, 13.6972f, 20.3975f, 13.4375f)
                curveTo(20.617f, 13.7924f, 20.9348f, 14.0498f, 21.46f, 14.0498f)
                curveTo(21.6664f, 14.0498f, 21.8395f, 14.0077f, 21.9883f, 13.9385f)
                curveTo(21.995f, 14.0415f, 22f, 14.1453f, 22f, 14.25f)
                verticalLineTo(15.25f)
                curveTo(22f, 17.2437f, 20.5412f, 18.8958f, 18.6328f, 19.1992f)
                curveTo(18.2015f, 20.8129f, 16.7319f, 21.9998f, 15.0059f, 22f)
                curveTo(13.7782f, 22f, 12.686f, 21.4091f, 12f, 20.4961f)
                curveTo(11.314f, 21.4091f, 10.2218f, 22f, 8.99414f, 22f)
                curveTo(7.26809f, 21.9998f, 5.79853f, 20.8129f, 5.36719f, 19.1992f)
                curveTo(3.45883f, 18.8958f, 2f, 17.2437f, 2f, 15.25f)
                verticalLineTo(15f)
                curveTo(2f, 13.4384f, 2.89526f, 12.0865f, 4.2002f, 11.4277f)
                curveTo(3.46102f, 10.7221f, 3f, 9.7276f, 3f, 8.625f)
                curveTo(3f, 6.68645f, 4.42349f, 5.08024f, 6.28223f, 4.79492f)
                curveTo(6.50358f, 3.21548f, 7.85955f, 2f, 9.5f, 2f)
                close()
                moveTo(9.5f, 3.5f)
                curveTo(8.5335f, 3.5f, 7.75f, 4.2835f, 7.75f, 5.25f)
                verticalLineTo(5.5f)
                curveTo(7.75f, 5.91421f, 7.41421f, 6.25f, 7f, 6.25f)
                horizontalLineTo(6.875f)
                curveTo(5.56332f, 6.25f, 4.5f, 7.31332f, 4.5f, 8.625f)
                curveTo(4.5f, 9.93668f, 5.56332f, 11f, 6.875f, 11f)
                horizontalLineTo(7.25f)
                curveTo(7.66421f, 11f, 8f, 11.3358f, 8f, 11.75f)
                curveTo(8f, 12.1642f, 7.66421f, 12.5f, 7.25f, 12.5f)
                curveTo(6.83322f, 12.5f, 6.4169f, 12.5f, 6f, 12.5f)
                curveTo(4.61929f, 12.5f, 3.5f, 13.6193f, 3.5f, 15f)
                verticalLineTo(15.25f)
                curveTo(3.5f, 16.6307f, 4.61929f, 17.75f, 6f, 17.75f)
                curveTo(6.38775f, 17.75f, 6.71175f, 18.0455f, 6.74707f, 18.4316f)
                curveTo(6.85289f, 19.5956f, 7.83586f, 20.4998f, 8.99414f, 20.5f)
                curveTo(10.2385f, 20.5f, 11.25f, 19.4909f, 11.25f, 18.25f)
                verticalLineTo(5.25f)
                curveTo(11.25f, 4.2835f, 10.4665f, 3.5f, 9.5f, 3.5f)
                close()
                moveTo(21.4844f, 8f)
                curveTo(21.5469f, 8f, 21.6081f, 8.01957f, 21.6592f, 8.05566f)
                curveTo(21.7101f, 8.09167f, 21.7487f, 8.14242f, 21.7695f, 8.20117f)
                lineTo(22.0186f, 8.9668f)
                curveTo(22.0959f, 9.19921f, 22.2271f, 9.41079f, 22.4004f, 9.58398f)
                curveTo(22.5737f, 9.75703f, 22.7851f, 9.88761f, 23.0176f, 9.96484f)
                lineTo(23.7832f, 10.2129f)
                lineTo(23.7979f, 10.2168f)
                curveTo(23.8568f, 10.2376f, 23.9082f, 10.2762f, 23.9443f, 10.3271f)
                curveTo(23.9805f, 10.3782f, 24f, 10.4394f, 24f, 10.502f)
                curveTo(24f, 10.5645f, 23.9805f, 10.6257f, 23.9443f, 10.6768f)
                curveTo(23.9082f, 10.7276f, 23.8567f, 10.7664f, 23.7979f, 10.7871f)
                lineTo(23.0332f, 11.0352f)
                curveTo(22.8005f, 11.1124f, 22.5884f, 11.2428f, 22.415f, 11.416f)
                curveTo(22.2417f, 11.5892f, 22.1116f, 11.8008f, 22.0342f, 12.0332f)
                lineTo(21.7852f, 12.7988f)
                curveTo(21.7643f, 12.8575f, 21.7257f, 12.9083f, 21.6748f, 12.9443f)
                curveTo(21.6237f, 12.9804f, 21.5626f, 13f, 21.5f, 13f)
                curveTo(21.4374f, 13f, 21.3763f, 12.9804f, 21.3252f, 12.9443f)
                curveTo(21.2743f, 12.9083f, 21.2357f, 12.8575f, 21.2148f, 12.7988f)
                lineTo(20.9658f, 12.0332f)
                curveTo(20.889f, 11.8002f, 20.7592f, 11.5879f, 20.5859f, 11.4141f)
                curveTo(20.4125f, 11.2401f, 20.1999f, 11.1089f, 19.9668f, 11.0312f)
                lineTo(19.2021f, 10.7832f)
                curveTo(19.1432f, 10.7624f, 19.0918f, 10.7238f, 19.0557f, 10.6729f)
                curveTo(19.0195f, 10.6218f, 19f, 10.5606f, 19f, 10.498f)
                curveTo(19f, 10.4355f, 19.0195f, 10.3743f, 19.0557f, 10.3232f)
                curveTo(19.0918f, 10.2724f, 19.1433f, 10.2337f, 19.2021f, 10.2129f)
                lineTo(19.9668f, 9.96484f)
                curveTo(20.1966f, 9.88555f, 20.4056f, 9.75415f, 20.5762f, 9.58105f)
                curveTo(20.7468f, 9.40795f, 20.8753f, 9.19762f, 20.9512f, 8.9668f)
                lineTo(21.1992f, 8.20117f)
                curveTo(21.22f, 8.14249f, 21.2588f, 8.0917f, 21.3096f, 8.05566f)
                curveTo(21.3606f, 8.01962f, 21.4219f, 8.00006f, 21.4844f, 8f)
                close()
                moveTo(17.4727f, 0f)
                curveTo(17.5852f, 0.0000437205f, 17.6952f, 0.0346543f, 17.7871f, 0.0996094f)
                curveTo(17.879f, 0.164603f, 17.9479f, 0.257194f, 17.9854f, 0.363281f)
                lineTo(18.4336f, 1.73926f)
                curveTo(18.5728f, 2.15781f, 18.808f, 2.5387f, 19.1201f, 2.85059f)
                curveTo(19.4322f, 3.16235f, 19.8127f, 3.39703f, 20.2314f, 3.53613f)
                lineTo(21.6094f, 3.9834f)
                lineTo(21.6367f, 3.99023f)
                curveTo(21.7429f, 4.02767f, 21.8353f, 4.0976f, 21.9004f, 4.18945f)
                curveTo(21.9653f, 4.28125f, 22f, 4.39149f, 22f, 4.50391f)
                curveTo(21.9999f, 4.61615f, 21.9652f, 4.72572f, 21.9004f, 4.81738f)
                curveTo(21.8353f, 4.90923f, 21.7429f, 4.97917f, 21.6367f, 5.0166f)
                lineTo(20.2588f, 5.46387f)
                curveTo(19.8401f, 5.603f, 19.4595f, 5.83765f, 19.1475f, 6.14941f)
                curveTo(18.8353f, 6.4613f, 18.6002f, 6.84219f, 18.4609f, 7.26074f)
                lineTo(18.0137f, 8.63672f)
                curveTo(18.0096f, 8.64816f, 18.0048f, 8.65981f, 18f, 8.6709f)
                curveTo(17.9605f, 8.7627f, 17.8965f, 8.84241f, 17.8145f, 8.90039f)
                curveTo(17.7225f, 8.96535f, 17.6126f, 9f, 17.5f, 9f)
                curveTo(17.3874f, 9f, 17.2775f, 8.96535f, 17.1855f, 8.90039f)
                curveTo(17.0936f, 8.83539f, 17.0238f, 8.74283f, 16.9863f, 8.63672f)
                lineTo(16.5391f, 7.26074f)
                curveTo(16.4379f, 6.95368f, 16.2848f, 6.66647f, 16.0879f, 6.41211f)
                curveTo(16.0157f, 6.3188f, 15.9373f, 6.22953f, 15.8535f, 6.14551f)
                curveTo(15.5415f, 5.83257f, 15.1606f, 5.59683f, 14.7412f, 5.45703f)
                lineTo(13.3633f, 5.00977f)
                curveTo(13.2571f, 4.97233f, 13.1647f, 4.9024f, 13.0996f, 4.81055f)
                curveTo(13.0347f, 4.71875f, 13f, 4.60851f, 13f, 4.49609f)
                curveTo(13.0001f, 4.38386f, 13.0348f, 4.27428f, 13.0996f, 4.18262f)
                curveTo(13.1647f, 4.09077f, 13.2571f, 4.02083f, 13.3633f, 3.9834f)
                lineTo(14.7412f, 3.53613f)
                curveTo(15.1548f, 3.39339f, 15.5299f, 3.15721f, 15.8369f, 2.8457f)
                curveTo(16.1356f, 2.54257f, 16.3623f, 2.17566f, 16.5f, 1.77344f)
                lineTo(16.5117f, 1.73926f)
                lineTo(16.959f, 0.363281f)
                curveTo(16.9964f, 0.257169f, 17.0663f, 0.164605f, 17.1582f, 0.0996094f)
                curveTo(17.2501f, 0.0347125f, 17.3601f, 0f, 17.4727f, 0f)
                close()
            }
        }.build()
    }

    val Translate by lazy {
        ImageVector
            .Builder(
                name = "translate",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 16f,
                viewportHeight = 16f,
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                ) {
                    moveTo(4.545f, 6.714f)
                    lineTo(4.11f, 8f)
                    horizontalLineTo(3f)
                    lineToRelative(1.862f, -5f)
                    horizontalLineToRelative(1.284f)
                    lineTo(8f, 8f)
                    horizontalLineTo(6.833f)
                    lineToRelative(-0.435f, -1.286f)
                    close()
                    moveToRelative(1.634f, -0.736f)
                    lineTo(5.5f, 3.956f)
                    horizontalLineToRelative(-0.049f)
                    lineToRelative(-0.679f, 2.022f)
                    close()
                }
                path(
                    fill = SolidColor(Color.Black),
                ) {
                    moveTo(0f, 2f)
                    arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                    horizontalLineToRelative(7f)
                    arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                    verticalLineToRelative(3f)
                    horizontalLineToRelative(3f)
                    arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                    verticalLineToRelative(7f)
                    arcToRelative(2f, 2f, 0f, false, true, -2f, 2f)
                    horizontalLineTo(7f)
                    arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                    verticalLineToRelative(-3f)
                    horizontalLineTo(2f)
                    arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                    close()
                    moveToRelative(2f, -1f)
                    arcToRelative(1f, 1f, 0f, false, false, -1f, 1f)
                    verticalLineToRelative(7f)
                    arcToRelative(1f, 1f, 0f, false, false, 1f, 1f)
                    horizontalLineToRelative(7f)
                    arcToRelative(1f, 1f, 0f, false, false, 1f, -1f)
                    verticalLineTo(2f)
                    arcToRelative(1f, 1f, 0f, false, false, -1f, -1f)
                    close()
                    moveToRelative(7.138f, 9.995f)
                    quadToRelative(0.289f, 0.451f, 0.63f, 0.846f)
                    curveToRelative(-0.748f, 0.575f, -1.673f, 1.001f, -2.768f, 1.292f)
                    curveToRelative(0.178f, 0.217f, 0.451f, 0.635f, 0.555f, 0.867f)
                    curveToRelative(1.125f, -0.359f, 2.08f, -0.844f, 2.886f, -1.494f)
                    curveToRelative(0.777f, 0.665f, 1.739f, 1.165f, 2.93f, 1.472f)
                    curveToRelative(0.133f, -0.254f, 0.414f, -0.673f, 0.629f, -0.89f)
                    curveToRelative(-1.125f, -0.253f, -2.057f, -0.694f, -2.82f, -1.284f)
                    curveToRelative(0.681f, -0.747f, 1.222f, -1.651f, 1.621f, -2.757f)
                    horizontalLineTo(14f)
                    verticalLineTo(8f)
                    horizontalLineToRelative(-3f)
                    verticalLineToRelative(1.047f)
                    horizontalLineToRelative(0.765f)
                    curveToRelative(-0.318f, 0.844f, -0.74f, 1.546f, -1.272f, 2.13f)
                    arcToRelative(6f, 6f, 0f, false, true, -0.415f, -0.492f)
                    arcToRelative(2f, 2f, 0f, false, true, -0.94f, 0.31f)
                }
            }.build()
    }

    val More by lazy {
        ImageVector
            .Builder(
                name = "more_horiz",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Companion.Black),
                ) {
                    moveTo(6f, 10f)
                    curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                    reflectiveCurveToRelative(0.9f, 2f, 2f, 2f)
                    reflectiveCurveToRelative(2f, -0.9f, 2f, -2f)
                    reflectiveCurveToRelative(-0.9f, -2f, -2f, -2f)
                    close()
                    moveTo(18f, 10f)
                    curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                    reflectiveCurveToRelative(0.9f, 2f, 2f, 2f)
                    reflectiveCurveToRelative(2f, -0.9f, 2f, -2f)
                    reflectiveCurveToRelative(-0.9f, -2f, -2f, -2f)
                    close()
                    moveTo(12f, 10f)
                    curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                    reflectiveCurveToRelative(0.9f, 2f, 2f, 2f)
                    reflectiveCurveToRelative(2f, -0.9f, 2f, -2f)
                    reflectiveCurveToRelative(-0.9f, -2f, -2f, -2f)
                    close()
                }
            }.build()
    }

    val Flag by lazy {
        ImageVector
            .Builder(
                name = "flag",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Companion.Black),
                ) {
                    moveTo(14.4f, 6f)
                    lineTo(14f, 4f)
                    horizontalLineTo(5f)
                    verticalLineToRelative(17f)
                    horizontalLineToRelative(2f)
                    verticalLineToRelative(-7f)
                    horizontalLineToRelative(5.6f)
                    lineToRelative(0.4f, 2f)
                    horizontalLineToRelative(7f)
                    verticalLineTo(6f)
                    horizontalLineToRelative(-5.6f)
                    close()
                }
            }.build()
    }

    val Close by lazy {
        ImageVector
            .Builder(
                name = "close",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Companion.Black),
                ) {
                    moveTo(19f, 6.41f)
                    lineTo(17.59f, 5f)
                    lineTo(12f, 10.59f)
                    lineTo(6.41f, 5f)
                    lineTo(5f, 6.41f)
                    lineTo(10.59f, 12f)
                    lineTo(5f, 17.59f)
                    lineTo(6.41f, 19f)
                    lineTo(12f, 13.41f)
                    lineTo(17.59f, 19f)
                    lineTo(19f, 17.59f)
                    lineTo(13.41f, 12f)
                    close()
                }
            }.build()
    }

    val Check by lazy {
        ImageVector
            .Builder(
                name = "check",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 16f,
                viewportHeight = 16f,
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                ) {
                    moveTo(10.97f, 4.97f)
                    arcToRelative(0.75f, 0.75f, 0f, false, true, 1.07f, 1.05f)
                    lineToRelative(-3.99f, 4.99f)
                    arcToRelative(0.75f, 0.75f, 0f, false, true, -1.08f, 0.02f)
                    lineTo(4.324f, 8.384f)
                    arcToRelative(0.75f, 0.75f, 0f, true, true, 1.06f, -1.06f)
                    lineToRelative(2.094f, 2.093f)
                    lineToRelative(3.473f, -4.425f)
                    close()
                }
            }.build()
    }

    val ArrowRight by lazy {
        ImageVector
            .Builder(
                name = "arrow_right",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Companion.Black),
                ) {
                    moveTo(16.01f, 11f)
                    horizontalLineTo(4f)
                    verticalLineToRelative(2f)
                    horizontalLineToRelative(12.01f)
                    verticalLineToRelative(3f)
                    lineTo(20f, 12f)
                    lineToRelative(-3.99f, -4f)
                    close()
                }
            }.build()
    }

    val Github by lazy {
        ImageVector
            .Builder(
                name = "github",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 16f,
                viewportHeight = 16f,
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                ) {
                    moveTo(8f, 0f)
                    curveTo(3.58f, 0f, 0f, 3.58f, 0f, 8f)
                    curveToRelative(0f, 3.54f, 2.29f, 6.53f, 5.47f, 7.59f)
                    curveToRelative(0.4f, 0.07f, 0.55f, -0.17f, 0.55f, -0.38f)
                    curveToRelative(0f, -0.19f, -0.01f, -0.82f, -0.01f, -1.49f)
                    curveToRelative(-2.01f, 0.37f, -2.53f, -0.49f, -2.69f, -0.94f)
                    curveToRelative(-0.09f, -0.23f, -0.48f, -0.94f, -0.82f, -1.13f)
                    curveToRelative(-0.28f, -0.15f, -0.68f, -0.52f, -0.01f, -0.53f)
                    curveToRelative(0.63f, -0.01f, 1.08f, 0.58f, 1.23f, 0.82f)
                    curveToRelative(0.72f, 1.21f, 1.87f, 0.87f, 2.33f, 0.66f)
                    curveToRelative(0.07f, -0.52f, 0.28f, -0.87f, 0.51f, -1.07f)
                    curveToRelative(-1.78f, -0.2f, -3.64f, -0.89f, -3.64f, -3.95f)
                    curveToRelative(0f, -0.87f, 0.31f, -1.59f, 0.82f, -2.15f)
                    curveToRelative(-0.08f, -0.2f, -0.36f, -1.02f, 0.08f, -2.12f)
                    curveToRelative(0f, 0f, 0.67f, -0.21f, 2.2f, 0.82f)
                    curveToRelative(0.64f, -0.18f, 1.32f, -0.27f, 2f, -0.27f)
                    reflectiveCurveToRelative(1.36f, 0.09f, 2f, 0.27f)
                    curveToRelative(1.53f, -1.04f, 2.2f, -0.82f, 2.2f, -0.82f)
                    curveToRelative(0.44f, 1.1f, 0.16f, 1.92f, 0.08f, 2.12f)
                    curveToRelative(0.51f, 0.56f, 0.82f, 1.27f, 0.82f, 2.15f)
                    curveToRelative(0f, 3.07f, -1.87f, 3.75f, -3.65f, 3.95f)
                    curveToRelative(0.29f, 0.25f, 0.54f, 0.73f, 0.54f, 1.48f)
                    curveToRelative(0f, 1.07f, -0.01f, 1.93f, -0.01f, 2.2f)
                    curveToRelative(0f, 0.21f, 0.15f, 0.46f, 0.55f, 0.38f)
                    arcTo(8.01f, 8.01f, 0f, false, false, 16f, 8f)
                    curveToRelative(0f, -4.42f, -3.58f, -8f, -8f, -8f)
                }
            }.build()
    }

    val School by lazy {
        ImageVector
            .Builder(
                name = "school",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Companion.Black),
                ) {
                    moveTo(5f, 13.18f)
                    verticalLineToRelative(4f)
                    lineTo(12f, 21f)
                    lineToRelative(7f, -3.82f)
                    verticalLineToRelative(-4f)
                    lineTo(12f, 17f)
                    lineToRelative(-7f, -3.82f)
                    close()
                    moveTo(12f, 3f)
                    lineTo(1f, 9f)
                    lineToRelative(11f, 6f)
                    lineToRelative(9f, -4.91f)
                    verticalLineTo(17f)
                    horizontalLineToRelative(2f)
                    verticalLineTo(9f)
                    lineTo(12f, 3f)
                    close()
                }
            }.build()
    }

    val DonateBox by lazy {
        ImageVector
            .Builder(
                name = "box2-heart",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 16f,
                viewportHeight = 16f,
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                ) {
                    moveTo(8f, 7.982f)
                    curveTo(9.664f, 6.309f, 13.825f, 9.236f, 8f, 13f)
                    curveTo(2.175f, 9.236f, 6.336f, 6.31f, 8f, 7.982f)
                }
                path(
                    fill = SolidColor(Color.Black),
                ) {
                    moveTo(3.75f, 0f)
                    arcToRelative(1f, 1f, 0f, false, false, -0.8f, 0.4f)
                    lineTo(0.1f, 4.2f)
                    arcToRelative(0.5f, 0.5f, 0f, false, false, -0.1f, 0.3f)
                    verticalLineTo(15f)
                    arcToRelative(1f, 1f, 0f, false, false, 1f, 1f)
                    horizontalLineToRelative(14f)
                    arcToRelative(1f, 1f, 0f, false, false, 1f, -1f)
                    verticalLineTo(4.5f)
                    arcToRelative(0.5f, 0.5f, 0f, false, false, -0.1f, -0.3f)
                    lineTo(13.05f, 0.4f)
                    arcToRelative(1f, 1f, 0f, false, false, -0.8f, -0.4f)
                    close()
                    moveToRelative(0f, 1f)
                    horizontalLineTo(7.5f)
                    verticalLineToRelative(3f)
                    horizontalLineToRelative(-6f)
                    close()
                    moveTo(8.5f, 4f)
                    verticalLineTo(1f)
                    horizontalLineToRelative(3.75f)
                    lineToRelative(2.25f, 3f)
                    close()
                    moveTo(15f, 5f)
                    verticalLineToRelative(10f)
                    horizontalLineTo(1f)
                    verticalLineTo(5f)
                    close()
                }
            }.build()
    }

    val EmailHeart by lazy {
        ImageVector
            .Builder(
                name = "mail-heart",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(10.5f, 19f)
                    horizontalLineToRelative(-5.5f)
                    arcToRelative(2f, 2f, 0f, false, true, -2f, -2f)
                    verticalLineToRelative(-10f)
                    arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
                    horizontalLineToRelative(14f)
                    arcToRelative(2f, 2f, 0f, false, true, 2f, 2f)
                    verticalLineToRelative(4f)
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(3f, 7f)
                    lineToRelative(9f, 6f)
                    lineToRelative(2.983f, -1.989f)
                    lineToRelative(6.017f, -4.011f)
                }
                path(
                    fill = SolidColor(Color.Transparent),
                    stroke = SolidColor(Color.Black),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                ) {
                    moveTo(18f, 22f)
                    lineToRelative(3.35f, -3.284f)
                    arcToRelative(2.143f, 2.143f, 0f, false, false, 0.005f, -3.071f)
                    arcToRelative(2.242f, 2.242f, 0f, false, false, -3.129f, -0.006f)
                    lineToRelative(-0.224f, 0.22f)
                    lineToRelative(-0.223f, -0.22f)
                    arcToRelative(2.242f, 2.242f, 0f, false, false, -3.128f, -0.006f)
                    arcToRelative(2.143f, 2.143f, 0f, false, false, -0.006f, 3.071f)
                    lineToRelative(3.355f, 3.296f)
                    close()
                }
            }.build()
    }

    val WordOrder: ImageVector by lazy {
        ImageVector.Builder(
            name = "low_priority",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Transparent)
            ) {
                moveTo(0f, 0f)
                horizontalLineToRelative(24f)
                verticalLineToRelative(24f)
                horizontalLineTo(0f)
                verticalLineTo(0f)
                close()
            }
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(14f, 5f)
                horizontalLineToRelative(8f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(-8f)
                verticalLineTo(5f)
                close()
                moveToRelative(0f, 5.5f)
                horizontalLineToRelative(8f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(-8f)
                verticalLineToRelative(-2f)
                close()
                moveToRelative(0f, 5.5f)
                horizontalLineToRelative(8f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(-8f)
                verticalLineToRelative(-2f)
                close()
                moveTo(2f, 11.5f)
                curveTo(2f, 15.08f, 4.92f, 18f, 8.5f, 18f)
                horizontalLineTo(9f)
                verticalLineToRelative(2f)
                lineToRelative(3f, -3f)
                lineToRelative(-3f, -3f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(-0.5f)
                curveTo(6.02f, 16f, 4f, 13.98f, 4f, 11.5f)
                reflectiveCurveTo(6.02f, 7f, 8.5f, 7f)
                horizontalLineTo(12f)
                verticalLineTo(5f)
                horizontalLineTo(8.5f)
                curveTo(4.92f, 5f, 2f, 7.92f, 2f, 11.5f)
                close()
            }
        }.build()
    }

    val VerbsShapes: ImageVector by lazy {
        ImageVector.Builder(
            name = "shapes",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8.3f, 10f)
                arcToRelative(0.7f, 0.7f, 0f, false, true, -0.626f, -1.079f)
                lineTo(11.4f, 3f)
                arcToRelative(0.7f, 0.7f, 0f, false, true, 1.198f, -0.043f)
                lineTo(16.3f, 8.9f)
                arcToRelative(0.7f, 0.7f, 0f, false, true, -0.572f, 1.1f)
                close()
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 14f)
                horizontalLineTo(9f)
                arcTo(1f, 1f, 0f, false, true, 10f, 15f)
                verticalLineTo(20f)
                arcTo(1f, 1f, 0f, false, true, 9f, 21f)
                horizontalLineTo(4f)
                arcTo(1f, 1f, 0f, false, true, 3f, 20f)
                verticalLineTo(15f)
                arcTo(1f, 1f, 0f, false, true, 4f, 14f)
                close()
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 17.5f)
                arcTo(3.5f, 3.5f, 0f, false, true, 17.5f, 21f)
                arcTo(3.5f, 3.5f, 0f, false, true, 14f, 17.5f)
                arcTo(3.5f, 3.5f, 0f, false, true, 21f, 17.5f)
                close()
            }
        }.build()
    }

    val ArticlesBook: ImageVector by lazy {
        ImageVector.Builder(
            name = "book-a",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 19.5f)
                verticalLineToRelative(-15f)
                arcTo(2.5f, 2.5f, 0f, false, true, 6.5f, 2f)
                horizontalLineTo(19f)
                arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
                verticalLineToRelative(18f)
                arcToRelative(1f, 1f, 0f, false, true, -1f, 1f)
                horizontalLineTo(6.5f)
                arcToRelative(1f, 1f, 0f, false, true, 0f, -5f)
                horizontalLineTo(20f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8f, 13f)
                lineToRelative(4f, -7f)
                lineToRelative(4f, 7f)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9.1f, 11f)
                horizontalLineToRelative(5.7f)
            }
        }.build()
    }
}

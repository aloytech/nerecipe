package ru.netology.nerecipe

data class Recipe(
    val id: Int,
    val authorId: String,
    val name: String,
    val categoryId: Int,
    val likesCount: Int,
    val servingLink: String,
    val stages: ArrayList<String>,
    val stagesLink: List<String>
) {
    fun likesToString(): String {
        return shortCountOut(likesCount)
    }

    private fun shortCountOut(count: Int): String {
        return when (count) {
            in 0..999 -> {
                count.toString()
            }
            in 1000..9999 -> {
                val s = (count / 1000).toString()
                val h = (count % 1000 / 100).toString()
                "$s,$h" + "K"
            }
            in 10000..999999 -> {
                val s = (count / 1000).toString()
                s + "K"
            }
            else -> {
                val m = (count / 1000000).toString()
                val s = (count % 1000000 / 100000)
                "$m,$s" + "M"
            }
        }
    }
}
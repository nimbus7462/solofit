class Quote {
    var quoteID: Int = 0
    var quoteText: String
    var quoteAuthor: String
    var isSaved: Boolean

    // Constructor without ID
    constructor(quoteText: String, quoteAuthor: String, isSaved: Boolean = false) {
        this.quoteText = quoteText
        this.quoteAuthor = quoteAuthor
        this.isSaved = isSaved
    }

    // Constructor with ID
    constructor(quoteID: Int, quoteText: String, quoteAuthor: String, isSaved: Boolean = false) {
        this.quoteID = quoteID
        this.quoteText = quoteText
        this.quoteAuthor = quoteAuthor
        this.isSaved = isSaved
    }

    override fun toString(): String {
        return "Quote{" +
                "quoteID=$quoteID" +
                ", quoteText='$quoteText'" +
                ", quoteAuthor='$quoteAuthor'" +
                ", isSaved=$isSaved" +
                '}'
    }
}

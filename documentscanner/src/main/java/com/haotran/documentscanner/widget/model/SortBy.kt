package com.haotran.documentscanner.widget.model

class SortBy {
    var id: Int? = null
    var title: String? = null
    var keyWord: String? = null

    constructor(id: Int?, title: String?, keyWord: String?) {
        this.id = id
        this.title = title
        this.keyWord = keyWord
    }
}
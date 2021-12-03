package com.jithinraj.noter

import java.util.HashMap

class NoteModel {
    var id: String? = null
    var title: String? = null
    var description: String? = null

    constructor() {}

    constructor(id: String, title: String, description: String) {
        this.id = id
        this.title = title
        this.description = description
    }

    constructor(title: String, description: String) {
        this.title = title
        this.description = description
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("title", title!!)
        result.put("description", description!!)

        return result
    }
}
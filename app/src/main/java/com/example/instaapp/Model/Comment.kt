package com.example.instaapp.Model

class Comment {
    private var publisher:String=""
    private var caption:String=""

    constructor()

    constructor(publisher: String, caption: String) {
        this.publisher = publisher
        this.caption = caption
    }

    fun getPublisher():String{
        return publisher
    }
    fun getCaption():String{
        return caption
    }

    fun setPublisher(publisher: String)
    {
        this.publisher=publisher
    }

    fun setCaption(caption: String)
    {
        this.caption=caption
    }
}
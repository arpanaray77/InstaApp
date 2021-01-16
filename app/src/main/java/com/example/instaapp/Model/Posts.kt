package com.example.instaapp.Model

class Posts {
    private var postid:String=""
    private var postimage:String=""
    private var publisher:String=""
    private var caption:String=""

    constructor()

    constructor(postid: String, postimage: String, publisher: String, caption: String) {
        this.postid = postid
        this.postimage = postimage
        this.publisher = publisher
        this.caption = caption
    }

}
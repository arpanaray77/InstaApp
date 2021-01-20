package com.example.instaapp.Model

class Story {
    private var imageurl:String=""
    private var storyid:String=""
    private var userid:String=""
    private var timestart:Long = 0
    private  var timeend:Long=0

    constructor()

    constructor(imageurl: String, storyid: String, userid: String, timestart: Long, timeend: Long) {
        this.imageurl = imageurl
        this.storyid = storyid
        this.userid = userid
        this.timestart = timestart
        this.timeend = timeend
    }

    //getters
    fun getImageurl():String{
        return imageurl
    }

    fun getStoryid():String{
        return storyid
    }
    fun getUserid():String{
        return userid
    }
    fun getTimestart():Long{
        return timestart
    }
    fun getTimeend():Long{
        return timeend
    }

    //setters
    fun setImageurl(imageurl: String)
    {
        this.imageurl=imageurl
    }

    fun setStoryid(storyid: String)
    {
        this.storyid=storyid
    }

    fun setUserid(userid: String)
    {
        this.userid=userid
    }

    fun setTimestart(timestart: Long)
    {
        this.timestart=timestart
    }
    fun setTimeend(timeend: Long)
    {
        this.timeend=timeend
    }

}
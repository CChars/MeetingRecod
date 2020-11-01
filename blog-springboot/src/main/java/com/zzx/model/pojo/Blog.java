package com.zzx.model.pojo;

import lombok.Data;
import lombok.ToString;
import sun.java2d.pipe.SolidTextRenderer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 博文
 */
@Data
@ToString(exclude = "body")
public class Blog implements Serializable {
    /**
     * blog(36) => 541312(10)
     */
    private static final long serialVersionUID = 541312L;
    /**
     * id
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    //###########################################
    /**
     * 会议日期
     */
    private Date meetingData;
    /**
     * 会议地点
     */
    private String meetingArea;
    /**
     * 参会人
     */
    private String attendance;
    /**
     * 领导人
     */
    private String leader;
    /**
     * 论文 URL
     */
    private String paperURL;
    /**
     * PPT URL
     */
    private String pptURL;
    /**
     * 会议记录视频链接
     */
    private String meetingVideoLink;

    //#######################################
    /**
     * 内容
     */
    private String body;
    //#######################################
    /**
     * 评论数
     */
    private Integer discussCount;

    /**
     * 浏览数
     */
    private Integer blogViews;

    /**
     * 发布时间
     */
    private Date time;
    /**
     * 博文状态--0删除 1正常
     */
    private Integer state;

    /**
     * 所属用户
     */
    private User user;
    /**
     * 博文对应的标签
     */
    private List<Tag> tags;

    /**
     * 博文点赞数
     */
    private Integer likeCount;

    public Blog() {
    }

    public Blog(int blogId, int likeCount) {
        this.id = blogId;
        this.likeCount = likeCount;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getMeetingData() {
        return meetingData;
    }

    public void setMeetingData(Date meetingData) {
        this.meetingData = meetingData;
    }

    public String getMeetingArea() {
        return meetingArea;
    }

    public void setMeetingArea(String meetingArea) {
        this.meetingArea = meetingArea;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getPaperURL() {
        return paperURL;
    }

    public void setPaperURL(String paperURL) {
        this.paperURL = paperURL;
    }

    public String getPptURL() {
        return pptURL;
    }

    public void setPptURL(String pptURL) {
        this.pptURL = pptURL;
    }

    public String getMeetingVideoLink() {
        return meetingVideoLink;
    }

    public void setMeetingVideoLink(String meetingVideoLink) {
        this.meetingVideoLink = meetingVideoLink;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getDiscussCount() {
        return discussCount;
    }

    public void setDiscussCount(Integer discussCount) {
        this.discussCount = discussCount;
    }

    public Integer getBlogViews() {
        return blogViews;
    }

    public void setBlogViews(Integer blogViews) {
        this.blogViews = blogViews;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}

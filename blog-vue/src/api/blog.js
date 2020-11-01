import request from '@/utils/request'
import qs from 'qs';

export default {
  getHotBlog() {
    return request({
      url: '/blog/hotBlog',
      method: 'get'
    })
  },
  getStatisticalBlogByMonth() {
    return request({
      url: '/blog/statisticalBlogByMonth',
      method: 'get'
    })
  },
  getBlogHome(page, showCount, tagId) {
    return request({
      url: '/blog/home/' + page + '/' + showCount + '/' + tagId,
      method: 'get'
    })
  },
  getBlogById(id, isClick) {
    return request({
      url: '/blog/' + id + '/' + isClick,
      method: 'get'
    })
  },
  getMyBlog(page, showCount) {
    return request({
      url: '/blog/myblog/' + page + '/' + showCount,
      method: 'get'
    })
  },
  sendBlog(blog, tagId) {  //发布会议
    console.log({'blog':blog,'tagId':tagId})
    return request({
      url: '/blog',
      method: 'post',
      data: {'blog':blog, 'tagId': tagId},
    })
  },
  uploadFile(formdata) {
    return request({
      url: '/blog/uploadFile',
      method: 'post',
      data: formdata,
      headers: {'Content-Type': 'multipart/form-data'},
    })
  },
  editBlog(blogId, blog, tagId) {  //发布会议
    return request({
      url: '/blog/' + blogId,
      method: 'put',
      // data: qs.stringify({'blogTitle': blogTitle, 'blogBody': blogBody, 'tagId': tagId})
      data: {'blog':blog, 'tagId': tagId}
    })
  },
  adminDeleteBlog(blogId) { //管理员删除
    return request({
      url: '/blog/admin/' + blogId,
      method: 'delete'
    })
  },
  userDeleteBlog(blogId) { //普通用户删除
    return request({
      url: '/blog/' + blogId,
      method: 'delete'
    })
  },
  adminGetBlog(page, showCount) {
    return request({
      url: '/blog/AllBlog/' + page + '/' + showCount,
      method: 'get'
    })
  },
  adminSearchBlog(searchTxt, page, showCount) {
    return request({
      url: '/blog/searchAllBlog/' + page + '/' + showCount + '?search=' + searchTxt,
      method: 'get'
    })
  },
  userSearchBlog(searchTxt, page, showCount) {
    return request({
      url: '/blog/searchBlog/' + page + '/' + showCount + '?search=' + searchTxt,
      method: 'get'
    })
  }
}

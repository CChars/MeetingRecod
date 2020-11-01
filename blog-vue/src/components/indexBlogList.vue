<template>
  <div id="indexBlogList" v-loading="loading">
    <div>
      <blogOverView
        v-for="blog in blogList"
        :key="blog.id"
        :id="blog.id"
        :title="blog.title"
        :body="blog.body"
        :leader="blog.leader"
        :meetingData="blog.meetingData"
        :meetingArea="blog.meetingArea"
        :time="getTime(blog.time)"
        :blogViews="blog.blogViews"
        :discussCount="blog.discussCount"
        :tags="catchTagName(blog.tags)"
        :name="blog.user.name"
      />
    </div>

    <div v-if="loading" style="margin: 35% 0"></div>
    <div v-show="getTagID"></div>
    <div>
      <el-pagination
        :pager-count="5"
        :page-size="pageSize"
        background
        layout="prev, pager, next"
        :total="total"
        @current-change="currentChange"
        :current-page="currentPage"
        @prev-click="currentPage = currentPage - 1"
        @next-click="currentPage = currentPage + 1"
      >
      </el-pagination>
    </div>
  </div>
</template>
<script>
import blogOverView from "@/components/blogOverView";
import blog from "@/api/blog";
import dateUtil from "@/utils/date";

export default {
  //时间转换filer
  name: "indexBlogList",
  //父组件中的标签ID
  props: ["tagID"],
  components: { blogOverView },
  data() {
    return {
      total: 0, //数据总数
      blogList: [], //当前页数据
      pageSize: 5, //每页显示数量
      currentPage: 1, //当前页数
      loading: true,
      tagId: 0,
    };
  },
  created() {
    // this.loadBlog();
  },
  computed: {
    getTagID() {
      if (this.tagID != "") {
        this.tagId = this.tagID;
        console.log(11111111);
        console.log(this.tagId);
        this.loadBlog();
        return this.tagId;
      } else {
        this.loadBlog();
        return '';
      }
    },
  },
  methods: {
    getTime(time) {
      //将时间戳转化为几分钟前，几小时前
      return dateUtil.timeago(time);
    },
    catchTagName(tag) {
      //从tag对象数组中拿到tag.Name属性

      var tagNames = [];
      for (var i = 0; i < tag.length; i++) {
        tagNames.push(tag[i].name);
      }
      return tagNames;
    },
    currentChange(currentPage) {
      //页码更改事件处理
      this.currentPage = currentPage;
      this.loadBlog();
      scrollTo(0, 0);
    },
    loadBlog() {
      // this.getTagID();
      console.log(22222222222);
      console.log(this.tagId);
      //加载数据
      blog.getBlogHome(this.currentPage, this.pageSize, this.tagId).then((responese) => {
        this.total = responese.data.total;
        this.blogList = responese.data.rows;
        console.log(9898989);
        console.log(this.blogList);
        this.blogList.forEach(blog => {
          var date = new Date(blog.meetingData);
          blog.meetingData = dateUtil.formatDate(date, "yyyy-MM-dd");
        });
        console.log(4444444);
        console.log(this.blogList);
        this.loading = false;
      });
    },
  },
};
</script>
<style scoped>
#indexBlogList {
  text-align: center;
  margin-top: -13px;
}
</style>

<template>
  <div id="newBlog">
    <el-card>
      <el-form
        :model="ruleForm"
        status-icon
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-row>
          <el-col :span="18">
            <el-form-item label="会议名称">
              <el-input
                type="text"
                v-model="ruleForm.title"
                placeholder="请输入会议名称"
                maxlength="100"
                show-word-limit
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="会议地点">
              <el-input
                type="text"
                v-model="ruleForm.meetingArea"
                placeholder="请输入会议地点"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="6">
            <el-form-item label="领导人">
              <el-input
                type="text"
                v-model="ruleForm.leader"
                placeholder="请输入领导人"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参会人">
              <el-input
                v-model="ruleForm.attendance"
                type="text"
                placeholder="请输入参会人"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="活动时间">
              <el-col :span="24">
                <el-form-item prop="meetingData">
                  <el-date-picker
                    type="date"
                    placeholder="选择日期"
                    v-model="ruleForm.meetingData"
                    style="width: 100%"
                  ></el-date-picker>
                </el-form-item>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <!-- 上传论文文件 -->
          <el-col :span="6">
            <el-form-item label="论文文档">
              <el-upload
                class="upload"
                action=""
                :before-remove="beforeRemove"
                :on-remove="onRemove"
                multiple
                :limit="1"
                :on-exceed="handleExceed"
                :file-list="PaperFilesList"
                :http-request="uploadSectionFile"
              >
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <!-- 上传PPT文件 -->
          <el-col :span="6">
            <el-form-item label="PPT文档">
              <el-upload
                class="upload"
                action=""
                :before-remove="beforeRemovePPT"
                :on-remove="onRemovePPT"
                multiple
                :limit="1"
                :on-exceed="handleExceedPPT"
                :file-list="PptFilesList"
                :http-request="PPTUploadSectionFile"
              >
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="会议视频链接">
              <el-input
                type="text"
                v-model="ruleForm.meetingVideoLink"
                placeholder="请输入链接"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 富文本编辑器 -->
        <br /><br />
        <mavon-editor
          ref="md"
          @save="save()"
          v-model="ruleForm.body"
          id="editor"
          @imgAdd="$uploadImg"
          @imgDel="$imgDel"
          placeholder="## Start"
        />

        <el-card id="tags" v-if="tags.length > 0">
          <div>
            <p class="el-icon-mouse">选择一个以上标签</p>
            <el-checkbox-group v-model="checkboxGroup">
              <el-checkbox
                v-for="tag in tags"
                :key="tag.id"
                :label="tag.id"
                border
                style="margin-top: 10px"
              >
                {{ tag.name }}
              </el-checkbox>
            </el-checkbox-group>
          </div>

          <el-button
            style="margin-top: 3%"
            type="primary"
            plain
            class="el-icon-document-checked"
            @click="sendBlog('ruleForm')"
          >
            发布
          </el-button>
        </el-card>

        <el-card v-if="tags.length <= 0"> 没有标签,请先添加标签 </el-card>

        <!-- <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')"
            >提交</el-button
          >
        </el-form-item> -->
      </el-form>

      <!-- <el-input
        type="text"
        placeholder="请输入标题"
        v-model="title"
        maxlength="100"
        show-word-limit
      /> -->

      <!-- <br /><br />
      <mavon-editor
        ref="md"
        @save="save()"
        v-model="body"
        id="editor"
        @imgAdd="$uploadImg"
        @imgDel="$imgDel"
        placeholder="## Start"
      /> -->

      <!-- 以下是预览模式配置 -->
      <!--:toolbarsFlag="false"  :subfield="false" defaultOpen="preview"-->
    </el-card>

    <!-- <el-card id="tags" v-if="tags.length > 0">
      <div>
        <p class="el-icon-mouse">选择一个以上标签</p>
        <el-checkbox-group v-model="checkboxGroup">
          <el-checkbox
            v-for="tag in tags"
            :key="tag.id"
            :label="tag.id"
            border
            style="margin-top: 10px"
          >
            {{ tag.name }}
          </el-checkbox>
        </el-checkbox-group>
      </div>

      <el-button
        style="margin-top: 3%"
        type="primary"
        plain
        class="el-icon-document-checked"
        @click="sendBlog"
      >
        发布
      </el-button>
    </el-card>

    <el-card v-if="tags.length <= 0"> 没有标签,请先添加标签 </el-card> -->
  </div>
</template>

<script>
import tag from "@/api/tag";
import blog from "@/api/blog";
import file from "@/utils/file";

export default {
  name: "newBlog",
  data() {
    return {
      // title: "",
      // body: "",
      tags: [],
      PaperFilesId: [],
      PptFilesId: [],
      PaperFilesList: [],
      PptFilesList: [],
      checkboxGroup: [],
      img_file: {},
      ruleForm: {
        title: "",
        meetingArea: "",
        leader: "",
        meetingData: "",
        attendance: "",
        body: "",
        meetingVideoLink: "",
        paperURL: "",
        pptURL: ""
      },
    };
  },
  methods: {
    // submitForm(formName) {
    //   this.$refs[formName].validate((valid) => {
    //     if (valid) {
    //       alert("submit!");
    //       console.log(this.ruleForm);
    //     } else {
    //       console.log("error submit!!");
    //       return false;
    //     }
    //   });
    // },
    sendBlog(formName) {
      //发布博客
      if (
        this.checkboxGroup.length <= 0 ||
        this.ruleForm.title.length <= 0 ||
        this.ruleForm.body.length <= 0
      ) {
        this.$message({
          type: "error",
          message: "字段不完整",
        });
        return;
      }
      var tags = this.checkboxGroup;
      var tagStr = [];
      for (var i = 0; i < tags.length; i++) {
        if (i != tags.length - 1) tagStr.push(tags[i]);
        else tagStr.push(tags[i]);
      }

      this.ruleForm.paperURL = this.PaperFilesList[0].url;
      this.ruleForm.pptURL = this.PptFilesList[0].url;

      console.log(this.ruleForm);
      blog.sendBlog(this.ruleForm, tagStr).then((res) => {
        this.$alert("发布成功", "提示", {
          confirmButtonText: "确定",
          callback: (action) => {
            if (action == "confirm") {
              scrollTo(0, 0);
              history.back();
            }
          },
        });
      });
    },
    $uploadImg(pos, $file) {
      //图片上传

      // 第一步.将图片上传到服务器.
      var formdata = new FormData();
      formdata.append("file", $file);
      blog.uploadFile(formdata).then((res) => {
        // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
        console.log(123424);
        console.log(res);
        this.$refs.md.$img2Url(pos, res.data);
      });
    },
    $imgDel(pos) {
      delete this.img_file[pos];
    },
    save() {
      if (this.ruleForm.title.length > 0 && this.ruleForm.body.length > 0) {
        file.generateTxt(
          this.ruleForm.title,
          this.ruleForm.body + "\n" + new Date().toLocaleString()
        );
      }
    },

    ///////////////////////////////////////////////////////////////////////
    //////////论文文件上传
    ////////////////////////////////////////////////////////////////////////
    handleExceed(files, PaperFilesId) {
      //超出限制
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
          files.length + PaperFilesId.length
        } 个文件`
      );
    },
    beforeRemove(file, PaperFilesId) {
      //确定移除

      return this.$confirm(`确定移除 ${file.name}？`);
    },
    onRemove(file, PaperFilesId) {
      //删除清空
      // console.log(file, fileList)
      this.PaperFilesId = [];
    },
    uploadSectionFile(param) {
      var fileObj = param.file;
      var formData = new FormData();
      formData.append("file", fileObj);
      console.log("formData", formData);
      // console.log('formData',fileObj.size)//可做大小限制等
      blog.uploadFile(formData).then((res) => {
        //请求数据
        if (res.code == 200) {
          console.log(555555);
          console.log(res.data);

          this.PaperFilesId.push(res.data);
          this.PaperFilesList.push({
            name: res.data,
            url: res.data,
          });
          console.log(this.PaperFilesId);
          this.$message.success(`上传成功`);
        } else {
          this.$message({
            message: res.message,
            type: "error",
          });
        }
      });
    },
    ///////////////////////////////////////////////////////////////////////
    //////////PPT文件上传
    ////////////////////////////////////////////////////////////////////////
    handleExceedPPT(files, PptFilesId) {
      //超出限制
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${
          files.length + PptFilesId.length
        } 个文件`
      );
    },
    beforeRemovePPT(file, PptFilesId) {
      //确定移除

      return this.$confirm(`确定移除 ${file.name}？`);
    },
    onRemovePPT(file, PptFilesId) {
      //删除清空
      // console.log(file, fileList)
      this.filesId = [];
    },
    PPTUploadSectionFile(param) {
      var fileObj = param.file;
      var formData = new FormData();
      formData.append("file", fileObj);
      console.log("formData", formData);
      // console.log('formData',fileObj.size)可做大小限制等
      blog.uploadFile(formData).then((res) => {
        //请求数据
        if (res.code == 200) {
          console.log(66666);
          console.log(res.data);

          this.PptFilesId.push(res.data);
          console.log(this.PptFilesId);

          this.PptFilesList.push({
            name: res.data,
            url: res.data,
          });
          console.log(this.PptFilesList);
          this.$message.success(`上传成功`);
        } else {
          this.$message({
            message: res.message,
            type: "error",
          });
        }
      });
    },
  },
  created() {
    tag.getTag().then((res) => {
      this.tags = res.data;
    });
  },
};
</script>
<style>
#newBlog {
  margin: 20px 5% 0 5%;
}

#tags {
  margin-top: 1%;
}

#editor {
  height: 600px;
}

.el-checkbox__input.is-checked + .el-checkbox__label {
  color: #67c23a;
}

.el-checkbox.is-bordered.is-checked {
  border-color: #67c23a;
}

.el-checkbox__input.is-checked .el-checkbox__inner,
.el-checkbox__input.is-indeterminate .el-checkbox__inner {
  background-color: #67c23a;
  border-color: #67c23a;
}
.upload {
  width: 80px;
}
</style>

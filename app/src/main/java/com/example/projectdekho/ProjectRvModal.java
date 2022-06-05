package com.example.projectdekho;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectRvModal implements Parcelable {

    private String projectName;
    private String projectDescription;
    private String batch;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    private String branch;
    private String category;
    private String projectImg;
    private String projectLink;
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public ProjectRvModal() {

    }

    protected ProjectRvModal(Parcel in) {
        projectName = in.readString();
        projectId = in.readString();
        projectDescription = in.readString();
        branch = in.readString();
        category = in.readString();
        batch = in.readString();
        projectImg = in.readString();
        projectLink = in.readString();
    }

    public static final Creator<ProjectRvModal> CREATOR = new Creator<ProjectRvModal>() {
        @Override
        public ProjectRvModal createFromParcel(Parcel in) {
            return new ProjectRvModal(in);
        }

        @Override
        public ProjectRvModal[] newArray(int size) {
            return new ProjectRvModal[size];
        }
    };

    public String getprojectName() {
        return projectName;
    }

    public void setprojectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getprojectImg() {
        return projectImg;
    }

    public void setprojectImg(String projectImg) {
        this.projectImg = projectImg;
    }

    public String getprojectLink() {
        return projectLink;
    }

    public void setprojectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }



    public ProjectRvModal(String projectId, String projectName, String projectDescription, String  bestSuitedFor ,String category, String batch, String projectImg, String projectLink) {
        this.projectName = projectName;
        this.projectId = projectId;
        this.projectDescription = projectDescription;
        this.branch = bestSuitedFor;
        this.category = category;
        this.batch = batch;
        this.projectImg = projectImg;
        this.projectLink = projectLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(projectName);
        dest.writeString(projectId);
        dest.writeString(projectDescription);
        dest.writeString(branch);
        dest.writeString(category);
        dest.writeString(batch);
        dest.writeString(projectImg);
        dest.writeString(projectLink);
    }
}




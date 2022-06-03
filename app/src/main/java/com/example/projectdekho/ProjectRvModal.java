package com.example.projectdekho;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectRvModal implements Parcelable {
    // creating variables for our different fields.
    private String projectName;
    private String projectDescription;
    private String category;
    private String projectImg;
    private String projectLink;
    private String courseId;


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    // creating an empty constructor.
    public ProjectRvModal() {

    }

    protected ProjectRvModal(Parcel in) {
        projectName = in.readString();
        courseId = in.readString();
        projectDescription = in.readString();
        category = in.readString();
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

    // creating getter and setter methods.
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


    public ProjectRvModal(String courseId, String projectName, String courseDescription, String coursePrice, String bestSuitedFor, String projectImg, String projectLink) {
        this.projectName = projectName;
        this.courseId = courseId;
        this.projectDescription = courseDescription;
        this.category = bestSuitedFor;
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
        dest.writeString(courseId);
        dest.writeString(projectDescription);
        dest.writeString(category);
        dest.writeString(projectImg);
        dest.writeString(projectLink);
    }


}


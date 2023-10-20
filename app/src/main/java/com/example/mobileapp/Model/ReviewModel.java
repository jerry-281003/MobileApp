package com.example.mobileapp.Model;

import java.util.Date;

public class ReviewModel {
    String ReviewId;
    String UserId;
    String Review;
    String UserName;
    String Date;
    String BusinessId;
    public String getBusinessId() {
        return BusinessId;
    }

    public void setBusinessId(String businessId) {
        BusinessId = businessId;
    }



    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }





    public String getReviewId() {
        return ReviewId;
    }

    public String getUserId() {
        return UserId;
    }

    public String getReview() {
        return Review;
    }

    public void setReviewId(String reviewId) {
        ReviewId = reviewId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setReview(String review) {
        Review = review;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }
}

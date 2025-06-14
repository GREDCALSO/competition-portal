package com.example.competition_portal.dto;

import com.example.competition_portal.entity.FavorFolder;
import com.example.competition_portal.entity.RefInfo;

import java.util.List;

public class FavoriteResponse {
    private boolean success;
    private String message;
    private List<RefInfo> favorites;
    private List<FavorFolder> folders;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<RefInfo> getFavorites() { return favorites; }
    public void setFavorites(List<RefInfo> favorites) { this.favorites = favorites; }
    public List<FavorFolder> getFolders() { return folders; }
    public void setFolders(List<FavorFolder> folders) { this.folders = folders; }
}
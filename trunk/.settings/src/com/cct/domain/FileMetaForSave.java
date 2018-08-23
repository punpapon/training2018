package com.cct.domain;

import java.io.File;

public class FileMetaForSave {
	private File fileUpload;
	private String fileUploadContentType;
	private String fileType;
	private String fileUploadedPath;
	private String fileUploadFileName;
	private String fileUploadFileNameTmp;
	private String fileThumbnail;
	private String fileSize;
	private String deleteFlag;

	private String id;
	private String remark;

	public File getFileUpload() {
		return fileUpload;
	}


	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}
	public String getFileUploadContentType() {
		return fileUploadContentType;
	}
	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}
	public String getFileUploadFileName() {
		return fileUploadFileName;
	}
	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}


	public String getFileThumbnail() {
		return fileThumbnail;
	}


	public void setFileThumbnail(String fileThumbnail) {
		this.fileThumbnail = fileThumbnail;
	}




	public String getFileUploadFileNameTmp() {
		return fileUploadFileNameTmp;
	}


	public void setFileUploadFileNameTmp(String fileUploadFileNameTmp) {
		this.fileUploadFileNameTmp = fileUploadFileNameTmp;
	}


	public String getFileSize() {
		return fileSize;
	}


	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}


	public String getDeleteFlag() {
		return deleteFlag;
	}


	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}


	public String getFileUploadedPath() {
		return fileUploadedPath;
	}


	public void setFileUploadedPath(String fileUploadedPath) {
		this.fileUploadedPath = fileUploadedPath;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
}

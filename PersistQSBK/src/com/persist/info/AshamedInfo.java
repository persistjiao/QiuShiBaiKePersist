package com.persist.info;

import java.io.Serializable;

public class AshamedInfo implements Serializable {
	private String qid;
	private String uid;
	private String tid;
	private String uname;
	private String uhead;
	private String qvalue;
	private String qlike;
	private String qunlike;
	private String qimg;
	private String qshare;

	public String getQshare() {
		return qshare;
	}

	public void setQshare(String qshare) {
		this.qshare = qshare;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUhead() {
		return uhead;
	}

	public void setUhead(String uhead) {
		this.uhead = uhead;
	}

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getQvalue() {
		return qvalue;
	}

	public void setQvalue(String qvalue) {
		this.qvalue = qvalue;
	}

	public String getQlike() {
		return qlike;
	}

	public void setQlike(String qlike) {
		this.qlike = qlike;
	}

	public String getQunlike() {
		return qunlike;
	}

	public void setQunlike(String qunlike) {
		this.qunlike = qunlike;
	}

	public String getQimg() {
		return qimg;
	}

	public void setQimg(String qimg) {
		this.qimg = qimg;
	}
}

package com.skymobi.cac.maopao.communication;

/**
 * 接入服务器连接状态。
 */
public enum AccessPipelineState {
	AUTHENTICATION, /* 认证流程中 */
	CONNECTED, /* 连接并认证成功 */
	IDLE /* 初始化状态 */
}

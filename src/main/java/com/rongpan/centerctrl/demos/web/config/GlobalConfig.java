package com.rongpan.centerctrl.demos.web.config;

public class GlobalConfig {
    public static boolean isRunnerSuccess = false;
    public static String centerIp = "192.168.0.178";


    public static String bytesToAscii(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            // 检查字节是否在ASCII范围内（0-127）
            if (b >= 0 && b <= 127) {
                // 将字节转换为字符并追加到StringBuilder
                sb.append((char) b);
            } else {
                // 对于非ASCII字节，可以选择替换为其他字符或忽略
                sb.append('?'); // 例如，用问号替换
            }
        }
        return sb.toString();
    }
}

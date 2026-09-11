package com.ruoyi.pms.constant;

public class PmsConstants
{
    public static final String DEL_NORMAL = "0";
    public static final String DEL_REMOVED = "2";

    public static final String STOCK_IN = "IN";
    public static final String STOCK_CHECK = "CHECK";
    public static final String STOCK_LOSS = "LOSS";
    public static final String STOCK_TRANSFER = "TRANSFER";
    public static final String STOCK_SALE = "SALE";
    public static final String STOCK_INIT = "INIT";
    public static final String STOCK_VOID = "VOID";
    public static final String STOCK_RETURN = "RETURN";

    public static final String BILL_NORMAL = "0";
    public static final String BILL_VOID = "1";

    public static final String PAY_UNCHECKED = "0";
    public static final String PAY_CHECKED = "1";
    public static final String PAY_SETTLED = "2";

    public static final String ONLINE_OFF = "0";
    public static final String ONLINE_ON = "1";

    public static final String SYNC_FAIL = "0";
    public static final String SYNC_OK = "1";
    public static final String SYNC_PENDING = "2";

    public static final String FLAG_NO = "0";
    public static final String FLAG_YES = "1";
    public static final String YES = "Y";
    public static final String NO = "N";

    public static final int HEARTBEAT_TIMEOUT_SEC = 120;

    /** 配置为 0 时的近实时轮询间隔（秒），V1 不接 MQTT */
    public static final int POLL_REALTIME_SEC = 5;

    public static int resolvePollIntervalSec(Integer configured)
    {
        if (configured == null || configured.intValue() <= 0)
        {
            return POLL_REALTIME_SEC;
        }
        return configured.intValue();
    }
}

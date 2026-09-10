package com.ruoyi.pms.util;

import java.util.Date;
import com.ruoyi.common.utils.DateUtils;

public class PmsNoUtils
{
    public static String next(String prefix, String maxNo)
    {
        String day = DateUtils.parseDateToStr("yyyyMMdd", new Date());
        int seq = 1;
        String head = prefix + day;
        if (maxNo != null && maxNo.startsWith(head) && maxNo.length() > head.length())
        {
            try
            {
                seq = Integer.parseInt(maxNo.substring(head.length())) + 1;
            }
            catch (NumberFormatException ignored)
            {
                seq = 1;
            }
        }
        return head + String.format("%04d", seq);
    }
}

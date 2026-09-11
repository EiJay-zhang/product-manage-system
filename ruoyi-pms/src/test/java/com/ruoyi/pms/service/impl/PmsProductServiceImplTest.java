package com.ruoyi.pms.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ruoyi.common.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
class PmsProductServiceImplTest
{
    @InjectMocks
    private PmsProductServiceImpl productService;

    @Test
    void importProduct_empty_rejected()
    {
        ServiceException ex = assertThrows(ServiceException.class,
            () -> productService.importProduct(Collections.emptyList(), false, "admin"));
        assertTrue(ex.getMessage().contains("导入数据不能为空"));
    }
}

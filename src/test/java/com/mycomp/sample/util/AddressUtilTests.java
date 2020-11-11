package com.mycomp.sample.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import com.mycomp.sample.util.AddressUtil;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class AddressUtilTests {

    @Test
    public void testAddress() {

        final String ip = AddressUtil.getAddress();

        log.info("# address : {}", ip);

        assertNotNull(ip);
    }

}

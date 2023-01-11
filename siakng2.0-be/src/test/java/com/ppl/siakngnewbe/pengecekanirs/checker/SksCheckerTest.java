package com.ppl.siakngnewbe.pengecekanirs.checker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.ppl.siakngnewbe.irsmahasiswa.IrsMahasiswa;
import com.ppl.siakngnewbe.pengecekanirs.result.SksResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SksCheckerTest {
    private SksChecker checker;

    private IrsMahasiswa now;
    private IrsMahasiswa before;

    private void initIrsMahasiswa() {
        now = new IrsMahasiswa();
        now.setSemester(2022);

        before = new IrsMahasiswa();
        before.setSemester(2021);
        before.setTotalMutu(2.67);
        before.setSksa(1);
    }

    @BeforeEach
    void setUp() {
        checker = new SksChecker();
    }

    @Nested
    class GetSksMaksimumFromIpsTest {
        @Test
        void testSksMaksimum24() throws Exception {
            assertEquals(24, checker.getSksMaksimumFromIps(3.75));
        }

        @Test
        void testSksMaksimum21() throws Exception {
            assertEquals(21, checker.getSksMaksimumFromIps(3.49));
        }

        @Test
        void testSksMaksimum18() throws Exception {
            assertEquals(18, checker.getSksMaksimumFromIps(2.5));
        }

        @Test
        void testSksMaksimum15() throws Exception {
            assertEquals(15, checker.getSksMaksimumFromIps(2));
        }

        @Test
        void testSksMaksimum12() throws Exception {
            assertEquals(12, checker.getSksMaksimumFromIps(0));
        }

        @Test
        void testIpsOutOfBound() throws Exception {
            assertThrows(IpsOutOfBoundException.class, () -> {
                checker.getSksMaksimumFromIps(4.91);
            });
        }
    }

    @Test
    void testCheckSingleOk() throws Exception {
        initIrsMahasiswa();
        now.setSksa(17);

        var result = new SksResult(2022, 4.00, 17, 24, true);
        assertEquals(result, checker.check(now));
    }    

    @Test
    void testCheckSingleNotOk() throws Exception {
        initIrsMahasiswa();
        now.setSksa(25);

        var result = new SksResult(2022, 4.00, 25, 24, false);
        assertEquals(result, checker.check(now));
    }

    @Test
    void testCheckPairOk() throws Exception {
        initIrsMahasiswa();
        now.setSksa(17);

        var result = new SksResult(2022, 2.67, 17, 18, true);
        assertEquals(result, checker.check(now, before));
    }

    @Test
    void testCheckPairNotOk() throws Exception {
        initIrsMahasiswa();
        now.setSksa(26);

        var result = new SksResult(2022, 2.67, 26, 18, false);
        assertEquals(result, checker.check(now, before));
    }
}

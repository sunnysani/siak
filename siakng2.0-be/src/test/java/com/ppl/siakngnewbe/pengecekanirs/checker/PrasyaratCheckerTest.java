package com.ppl.siakngnewbe.pengecekanirs.checker;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ppl.siakngnewbe.matakuliah.MataKuliah;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratProxy;
import com.ppl.siakngnewbe.pengecekanirs.result.PrasyaratResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PrasyaratCheckerTest {
    private PrasyaratChecker checker;

    private PrasyaratResult result;
    private List<MataKuliah> listMataKuliah;
    private Map<String, MataKuliah> mataKuliahLulus;

    private void initListMataKuliah() {
        listMataKuliah = new ArrayList<>();

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(0).setNama("Adapter");
        listMataKuliah.get(0).setId("ADAD000000");

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(1).setNama("Singleton");
        listMataKuliah.get(1).setId("SISI000000");

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(2).setNama("Command");
        listMataKuliah.get(2).setId("COCO000000");

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(3).setNama("Iterator");
        listMataKuliah.get(3).setId("ITIT000000");

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(4).setNama("Facade");
        listMataKuliah.get(4).setId("FAFA000000");

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(5).setNama("Factory");
        listMataKuliah.get(5).setId("FTFT000000");

        listMataKuliah.add(new MataKuliah());
        listMataKuliah.get(6).setNama("Visitor");
        listMataKuliah.get(6).setId("VIVI000000");

        listMataKuliah.get(0).setPrasyaratMataKuliahSet(Collections.emptySet());
        listMataKuliah.get(1).setPrasyaratMataKuliahSet(Set.of(listMataKuliah.get(4)));
        listMataKuliah.get(2).setPrasyaratMataKuliahSet(Set.of(listMataKuliah.get(5), listMataKuliah.get(6)));
        listMataKuliah.get(3).setPrasyaratMataKuliahSet(Set.of(listMataKuliah.get(6)));
        listMataKuliah.get(4).setPrasyaratMataKuliahSet(Collections.emptySet());
        listMataKuliah.get(5).setPrasyaratMataKuliahSet(Collections.emptySet());
        listMataKuliah.get(6).setPrasyaratMataKuliahSet(Collections.emptySet());
    }

    @BeforeEach
    void setUp() {
        checker = new PrasyaratChecker();

        initListMataKuliah();
    }

    @Nested
    class LulusSemuaPrasyaratTest {
        private PrasyaratProxy proxy;

        @BeforeEach
        void setUp() {
            mataKuliahLulus = Map.ofEntries(
                Map.entry("FAFA000000", listMataKuliah.get(4)),
                Map.entry("FTFT000000", listMataKuliah.get(5)),
                Map.entry("VIVI000000", listMataKuliah.get(6))
            );
        }
        
        @Test
        void testKelasAdapterOk() throws Exception {
            result = new PrasyaratResult(listMataKuliah.get(0));
            result.setNamaMataKuliah("Adapter");
            result.setIdMataKuliah("ADAD000000");
            result.setPrasyarat(Collections.emptyList());
            result.setOk(true);

            assertThat(checker.check(listMataKuliah.get(0), mataKuliahLulus)).usingRecursiveComparison()
                                                                             .ignoringCollectionOrder().isEqualTo(result);
        }

        @Test
        void testKelasSingletonOk() throws Exception {
            result = new PrasyaratResult(listMataKuliah.get(1));
            result.setNamaMataKuliah("Singleton");
            result.setIdMataKuliah("SISI000000");
            result.setPrasyarat(new ArrayList<>());
            
            proxy = new PrasyaratProxy(listMataKuliah.get(4));
            proxy.setLulus(true);
            result.getPrasyarat().add(proxy);

            result.setOk(true);

            assertThat(checker.check(listMataKuliah.get(1), mataKuliahLulus)).usingRecursiveComparison()
                                                                             .ignoringCollectionOrder().isEqualTo(result);
        }

        @Test
        void testKelasCommandOk() throws Exception {
            result = new PrasyaratResult(listMataKuliah.get(2));
            result.setNamaMataKuliah("Command");
            result.setIdMataKuliah("COCO000000");
            result.setPrasyarat(new ArrayList<>());
            
            proxy = new PrasyaratProxy(listMataKuliah.get(5));
            proxy.setLulus(true);
            result.getPrasyarat().add(proxy);
            
            proxy = new PrasyaratProxy(listMataKuliah.get(6));
            proxy.setLulus(true);
            result.getPrasyarat().add(proxy);

            result.setOk(true);

            assertThat(checker.check(listMataKuliah.get(2), mataKuliahLulus)).usingRecursiveComparison()
                                                                             .ignoringCollectionOrder().isEqualTo(result);
        }

        @Test
        void testKelasIteratorOk() throws Exception {
            result = new PrasyaratResult(listMataKuliah.get(3));
            result.setNamaMataKuliah("Iterator");
            result.setIdMataKuliah("ITIT000000");
            result.setPrasyarat(new ArrayList<>());
            
            proxy = new PrasyaratProxy(listMataKuliah.get(6));
            proxy.setLulus(true);
            result.getPrasyarat().add(proxy);

            result.setOk(true);

            assertThat(checker.check(listMataKuliah.get(3), mataKuliahLulus)).usingRecursiveComparison()
                                                                             .ignoringCollectionOrder().isEqualTo(result);
        }
    }
    
    @Nested
    class TidakLulusVisitorTest {
        private PrasyaratProxy proxy;

        @BeforeEach
        void setUp() {
            mataKuliahLulus = Map.ofEntries(
                Map.entry("FAFA000000", listMataKuliah.get(4)),
                Map.entry("FTFT000000", listMataKuliah.get(5))
            );
        }
        
        @Test
        void testKelasAdapterOk() throws Exception {
            result = new PrasyaratResult(listMataKuliah.get(0));
            result.setNamaMataKuliah("Adapter");
            result.setIdMataKuliah("ADAD000000");
            result.setPrasyarat(new ArrayList<>());
            result.setOk(true);

            assertThat(checker.check(listMataKuliah.get(0), mataKuliahLulus)).usingRecursiveComparison()
                                                                             .ignoringCollectionOrder().isEqualTo(result);
        }

        @Test
        void testKelasSingletonOk() throws Exception {
            result = new PrasyaratResult(listMataKuliah.get(1));
            result.setNamaMataKuliah("Singleton");
            result.setIdMataKuliah("SISI000000");
            result.setPrasyarat(new ArrayList<>());
            
            proxy = new PrasyaratProxy(listMataKuliah.get(4));
            proxy.setLulus(true);
            result.getPrasyarat().add(proxy);

            result.setOk(true);

            assertThat(checker.check(listMataKuliah.get(1), mataKuliahLulus)).usingRecursiveComparison()
                                                                             .ignoringCollectionOrder().isEqualTo(result);
        }

        @Test
        void testKelasCommandPrasyaratVisitorNotLulus() throws Exception {
            result = new PrasyaratResult(listMataKuliah.get(2));
            result.setNamaMataKuliah("Command");
            result.setIdMataKuliah("COCO000000");
            result.setPrasyarat(new ArrayList<>());
            
            proxy = new PrasyaratProxy(listMataKuliah.get(5));
            proxy.setLulus(true);
            result.getPrasyarat().add(proxy);
            
            proxy = new PrasyaratProxy(listMataKuliah.get(6));
            proxy.setLulus(false);
            result.getPrasyarat().add(proxy);

            result.setOk(false);

            assertThat(checker.check(listMataKuliah.get(2), mataKuliahLulus)).usingRecursiveComparison()
                                                                             .ignoringCollectionOrder().isEqualTo(result);
        }

        @Test
        void testKelasIteratorPrasyaratVisitorNotLulus() throws Exception {
            result = new PrasyaratResult(listMataKuliah.get(3));
            result.setNamaMataKuliah("Iterator");
            result.setIdMataKuliah("ITIT000000");
            result.setPrasyarat(new ArrayList<>());
            
            proxy = new PrasyaratProxy(listMataKuliah.get(6));
            proxy.setLulus(false);
            result.getPrasyarat().add(proxy);

            result.setOk(false);

            assertThat(checker.check(listMataKuliah.get(3), mataKuliahLulus)).usingRecursiveComparison()
                                                                             .ignoringCollectionOrder().isEqualTo(result);
        }
    }
}

package com.app.invoice.configs.tenants;


import com.app.invoice.master.entity.Business;
import com.app.invoice.master.services.BusinessService;
import com.app.invoice.tenant.dto.BusinessResponse;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantDataSourceProvider {
//    @Autowired
    private final EntityManagerFactoryBuilder entityManagerFactoryBuilder;
    private final BusinessService schoolService;
    private final Map<Object, Object>  tenantDataSources = new ConcurrentHashMap<>();

    public TenantDataSourceProvider(BusinessService schoolService, EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        this.schoolService = schoolService;
        this.entityManagerFactoryBuilder = entityManagerFactoryBuilder;
        createDataSourceForTenant("public");
        for (BusinessResponse business : schoolService.getAllCompanies()) {
            createDataSourceForTenant(String.valueOf(business.getId()));
        }
    }

    public DataSource getDataSource(String tenantId) {
        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = "public";
        }
        if (!tenantDataSources.containsKey(tenantId)) {
            createDataSourceForTenant(tenantId);
        }
        return (DataSource) tenantDataSources.get(tenantId);
    }

    private DataSource createDataSourceForTenant(String tenantId) {
        HikariDataSource dataSource;
        if (tenantId.equalsIgnoreCase("public")) {
            dataSource = DataSourceBuilder.create()
                    .type(HikariDataSource.class)
                    .url("jdbc:mysql://localhost/pharmacy_tenant_db?createDatabaseIfNotExist=true")
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .username("Snowbit")
                    .password("jfm15.ml")
                    .build();
        } else {
            Business pharmacy = schoolService.findByBusinessId(UUID.fromString(String.valueOf(tenantId)));
            dataSource = DataSourceBuilder.create()
                    .type(HikariDataSource.class)
                    .url(pharmacy.getDbUrl())
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .username("Snowbit")
                    .password("jfm15.ml")
                    .build();
        }

        dataSource.setMinimumIdle(2);
        dataSource.setMaximumPoolSize(5);
        dataSource.setIdleTimeout(60000);
        dataSource.setMaxLifetime(300000);
        dataSource.setConnectionTimeout(10000);
        dataSource.setLeakDetectionThreshold(15000);
        dataSource.setKeepaliveTime(30000);

        tenantDataSources.put(tenantId, dataSource);
        generateTenantSchema(dataSource);
        return dataSource;
    }

    public DataSource createDataSourceForTenant(Business business) {
        DataSource dataSource;
        dataSource = DataSourceBuilder.create()
                .url(business.getDbUrl())
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .username("Snowbit")
                .password("jfm15.ml")
                .build();
        tenantDataSources.put(business.getId(), dataSource);
        generateTenantSchema(dataSource);
        return dataSource;
    }

    private void generateTenantSchema(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.app.invoice.tenant.entity")
                .persistenceUnit("tenant")
                .properties(Map.of("hibernate.hbm2ddl.auto", "update"))
                .build();

        factoryBean.afterPropertiesSet();
    }
    public Map<Object, Object> getAllDataSources() {
        return tenantDataSources;
    }
}

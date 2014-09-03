package com.prueba.miniature.batman.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Dennis Hernández djhv92@hotmail.com
 * @version 1.1
 * @since 2014-03-01
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories("com.prueba.miniature.batman.repositories")
public class HibernateConfig {

    private final String driverClassName = "com.mysql.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/motorlinkdb";
    private final String username = "root"; 
    private final String password = "mtrlnk";
    private final Database dataBase = Database.MYSQL;
    
    /**
     * Obtiene el datasource de mySql para acceder al servidor de base de datos
     * @return ds Datasource con las credenciales para acceso al servidor de base de datos
     */
    @Bean()    
    public DataSource getDataSource()
    {
        DriverManagerDataSource ds = new DriverManagerDataSource();        
        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);        
        return ds;
    }
    
    /**
     * PersistenceExceptionTranslator capable of translating HibernateException instances to Spring's DataAccessException hierarchy. 
     * @return HibernateExceptionTranslator
     */
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() 
    {
        return new HibernateExceptionTranslator();
    }
    
    /**
     * Configura la creacion de base de datos desde los ejbs
     * @param dataSource Credenciales de acceso al servidor de base de datos
     * @return EntityManagerFactory Interface used to interact with the entity manager factory for the persistence unit.
     */
    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) 
    {
    	HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(false);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
        vendorAdapter.setDatabase(dataBase);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.prueba.miniature.batman.ejb");
        factory.setDataSource(dataSource);

        Properties properties = new Properties();
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        properties.setProperty("hibernate.cache.use_query_cache", "true");
        properties.setProperty("hibernate.generate_statistics", "true");
        
        factory.setJpaProperties(properties);
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    /**
     * Maneja las transacciones
     * @param entityManagerFactory Interface used to interact with the entity manager factory for the persistence unit.
     * @return JpaTransactionManager Binds a JPA EntityManager from the specified factory to the thread, 
     * potentially allowing for one thread-bound EntityManager per factory
     */
    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) 
    {        
    	JpaTransactionManager txManager = new JpaTransactionManager();
        JpaDialect jpaDialect = new HibernateJpaDialect();
        txManager.setEntityManagerFactory(entityManagerFactory);
        txManager.setJpaDialect(jpaDialect);
        return txManager;
    }
    
    /**
     * Crea sesion para el acceso a la aplicación
     * @param entityManagerFactory Interface used to interact with the entity manager factory for the persistence unit.
     * @return SessionFactory The main contract here is the creation of Session instances
     */
    @Bean
    @Autowired
    public SessionFactory getSessionFactory(EntityManagerFactory entityManagerFactory)
    {
        return ((HibernateEntityManagerFactory)entityManagerFactory).getSessionFactory();
    }
}
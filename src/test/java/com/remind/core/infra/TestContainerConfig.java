package com.remind.core.infra;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;



@Testcontainers
@ActiveProfiles("test")
public abstract class TestContainerConfig {

    /**
     * Mysql Test Container 생성
     */
    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8")
            .withExposedPorts(3306)
            .withCreateContainerCmdModifier(e ->
                    e.withHostConfig(
                            new HostConfig().withPortBindings(
                                    new PortBinding(Ports.Binding.bindPort(3306), new ExposedPort(3308)))
                    ));
    /**
     * Redis Test Container 생성
     */
    @Container
    static GenericContainer<?> redisContainer = new GenericContainer<>(
            DockerImageName.parse("redis:6.2.6"))
            .withExposedPorts(6379);

    /**
     * register dynamic properties to be added to the set of PropertySources in the Environment for an
     * ApplicationContext loaded for an integration test
     */
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    /**
     * register dynamic properties to be added to the set of PropertySources in the Environment for an
     * ApplicationContext loaded for an integration test
     */
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getFirstMappedPort());
    }

    /**
     * 실행 시점에 .env 파일 내용을 가져와 환경 변수 등록
     */
    @DynamicPropertySource
    static void envProperties(DynamicPropertyRegistry registry) {
        Dotenv.configure()
                .directory("./")
                .filename(".env")
                .load()
                .entries()
                .forEach(e -> {
                    registry.add(e.getKey(), e::getValue);
                });
    }

    /**
     * 테스트 실행 전 수행
     */
    @BeforeAll
    static void beforeAll(){
        mySQLContainer.start();
        redisContainer.start();
    }

    /**
     * 테스트 실행 후 수행
     */
    @AfterAll
    static void afterAll(){
        mySQLContainer.stop();
        redisContainer.stop();
    }
}

package io.pivotal.pal.tracker.backlog;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;
    private ConcurrentMap<Long,ProjectInfo> cache = new ConcurrentHashMap<Long,ProjectInfo>();


    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        cache.put(projectId, restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class));
        return cache.get(projectId);
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProjectFromCache(long projectId) {
        cache.put(projectId, restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class));
        return cache.get(projectId);
    }
}

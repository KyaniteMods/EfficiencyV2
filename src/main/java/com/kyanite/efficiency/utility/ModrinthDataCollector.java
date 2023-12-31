package com.kyanite.efficiency.utility;

import masecla.modrinth4j.endpoints.SearchEndpoint;
import masecla.modrinth4j.endpoints.version.GetProjectVersions;
import masecla.modrinth4j.main.ModrinthAPI;
import masecla.modrinth4j.model.project.Project;
import masecla.modrinth4j.model.tags.Category;
import masecla.modrinth4j.model.version.ProjectVersion;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ModrinthDataCollector {
    private ModrinthAPI client;
    public ModrinthDataCollector(ModrinthAPI client) {
        this.client = client;
    }

    public List<Category> getCategories() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Category>> categoriesAsync = client.tags().getCategories();
        return categoriesAsync.get();
    }

    public List<ProjectVersion> getVersions(String slug) throws ExecutionException, InterruptedException {
        CompletableFuture<List<ProjectVersion>> categoriesAsync = client.versions().getProjectVersions(slug, GetProjectVersions.GetProjectVersionsRequest.builder().build());
        return categoriesAsync.get();
    }

    public List<Project> getProjects(String... projects) throws ExecutionException, InterruptedException {
        CompletableFuture<List<Project>> categoriesAsync = client.projects().get(projects);
        return categoriesAsync.get();
    }

    public List<SearchEndpoint.SearchResult> getProjects(SearchEndpoint.SearchRequest searchRequest) throws ExecutionException, InterruptedException {
        CompletableFuture<SearchEndpoint.SearchResponse> categoriesAsync = client.search(searchRequest);
        return List.of(categoriesAsync.get().getHits());
    }
}

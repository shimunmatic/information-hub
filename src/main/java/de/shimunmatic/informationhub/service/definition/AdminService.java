package de.shimunmatic.informationhub.service.definition;

public interface AdminService {

    boolean deleteAllForProcessedDate(String dateFormatted);

    void forceFetch();

    void forceRemoveCacheForDailyUpdate();

    void forceRemoveAllCache();
}

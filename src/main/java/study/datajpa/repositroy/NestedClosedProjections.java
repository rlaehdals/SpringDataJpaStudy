package study.datajpa.repositroy;

public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();
    interface TeamInfo{
        String getName();
    }
}

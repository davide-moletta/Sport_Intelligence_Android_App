public class Match {

    public String firstPlayer, secondPlayer, result, location;

    public Match(String firstPlayer, String secondPlayer, String result, String location) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.result = result;
        this.location = location;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public String getResult() {
        return result;
    }

    public String getLocation() {
        return location;
    }
}

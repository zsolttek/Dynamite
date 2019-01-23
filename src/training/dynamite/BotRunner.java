package training.dynamite;

import com.softwire.dynamite.runner.*;

public class BotRunner {
    public static void main(String[] args) {
        Results results = DynamiteRunner.playGames(MyBot::new);
    }
}

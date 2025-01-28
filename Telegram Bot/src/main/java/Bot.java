import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Locality;
import org.telegram.abilitybots.api.objects.Privacy;
import org.telegram.abilitybots.api.toggle.BareboneToggle;

public class Bot extends AbilityBot {

    public static String BOT_TOKEN = "<BOT TOKEN>";
    public static String BOT_USERNAME = "SportIntelligenceBot";
    public static FireBase fireBase = new FireBase();

    private static final String helpString = "ECCO TUTTI I COMANDI DISPONIBILI\n\n" +
            "/help \t Mostra i comandi disponibili\n" +
            "/filtri \t Mostra i filtri creati tramite applicazione\n" +
            "/partite \t Mostra le partite preferite salvate tramite apllicazione";

    //Disattiva tutti i comandi base del bot
    private static final BareboneToggle toggle = new BareboneToggle();

    public Bot() {
        super(BOT_TOKEN, BOT_USERNAME, toggle);
    }

    @Override
    public long creatorId() {
        return 133504999;
    }

    //Crea un comando /filtri che permette di visualizzare i filtri
    public Ability showFilters() {
        return Ability
                .builder()
                .name("filtri")
                .info("Mostra tutti i filtri creati tramite l'applicazione")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> silent.send(fireBase.getData(String.valueOf(ctx.chatId()), "Filters"), ctx.chatId()))
                .build();
    }

    //Crea un comando /partite che permette di visualizzare le partite preferite
    public Ability showFavouriteMatches() {
        return Ability
                .builder()
                .name("partite")
                .info("Mostra tutte le partite aggiunte ai preferiti tramite l'applicazione")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> silent.send(fireBase.getData(String.valueOf(ctx.chatId()), "FavouriteMatches"), ctx.chatId()))
                .build();
    }

    //Crea un comando /help che permette di visualizzare tutti i comandi disponibili
    public Ability help() {
        return Ability
                .builder()
                .name("help")
                .info("Mostra tutti i comandi disponibili")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(ctx -> silent.send(helpString, ctx.chatId()))
                .build();
    }
}
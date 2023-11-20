package angga7togk.luckycrates.language;

import angga7togk.luckycrates.LuckyCrates;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Languages {

    @Getter
    private final String lang;
    public Languages(){
        String lang = LuckyCrates.cfg.getString("language");
        if(lang.equalsIgnoreCase("id") || lang.equalsIgnoreCase("en")){
            this.lang = lang;
        }else{
            this.lang = "en";
        }
    }

    public Map<String, String> getLanguage(){
        Map<String, Map<String, String>> lang = new HashMap<>();

        Map<String, String> id = new HashMap<>();
        Map<String, String> en = new HashMap<>();

        id.put("crate-notfound", "§cCrate tidak di temukan!");

        lang.put("id", id);
        lang.put("en", en);

        return lang.get(getLang());
    }
}

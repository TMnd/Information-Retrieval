package grupori.projectoriultimo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author João Amaral
 * @author Mafalda Rodrigues
 */
public enum groups {
    GROUP_0("^[0-9]\\w*"),
    GROUP_a("^a\\w*"),
    GROUP_bc("^[b-c]\\w*"),
    GROUP_de("^[d-e]\\w*"),
    GROUP_fg("^[f-g]\\w*"),
    GROUP_h("^h\\w*"),
    GROUP_i("^i\\w*"),
    GROUP_kjl("^[j-l]\\w*"),
    GROUP_mn("^[m-n]\\w*"),
    GROUP_o("^o\\w*"),
    GROUP_pqr("^[p-r]\\w*"),
    GROUP_s("^s\\w*"),
    GROUP_t("^t\\w*"),
    GROUP_uv("^[u-v]\\w*"),
    GROUP_w("^w\\w*"),
    GROUP_xyz("^[x-z]\\w*");
    
    String regex;

    groups(String regex) {
        this.regex = regex;
    }

    public String regex(){
        return regex;
    }
    
    public boolean matchesGroup(groups g, String key){
        Pattern padrao = Pattern.compile(g.regex());
        Matcher matcher = padrao.matcher(key);
        while (matcher.find()){
            return true;
        }
        return false;
    }
    
    public static String getGroupInitial(groups abbr){
    for(groups g : values()){
        if(g.equals(abbr)){
            String[] teste = g.toString().split("_");
            return teste[1];
        }
    }
    return null;
    }  
}

package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/05/2014
 */
public class ItemTokenParser {

    public interface ItemTypeMapper {
        ItemParser getParser(String typeName);
    }


    public interface ItemParser {
        Optional<Item> parseItem(String content);
    }

    public List<Item<?>> parse(ItemToken token, ItemTypeMapper itemTypeMapper) {
        List<Item<?>> result = Lists.newArrayList();
//        if("Path".endsWith(token.getTypeName())) {
//            List<ItemToken> subTokens = new ItemTokenizer().parseTokens(token.getItemContent());
//            for(ItemToken subToken : subTokens) {
//                List<Item<?>> path = parse(subToken, itemTypeMapper);
//                result.add(path);
//            }
//        }
//        else {
            ItemParser itemParser = itemTypeMapper.getParser(token.getTypeName());
            if(itemParser != null) {
                Optional<Item> item = itemParser.parseItem(token.getItemContent());
                if(item.isPresent()) {
                    result.add(item.get());
                }
            }
//        }
        return result;
    }
}

package edu.stanford.bmir.protege.web.client.place;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/05/2014
 */
public class ItemTokenParser {

    public static interface ItemTypeMapper {

        <T> ItemParser<T> getParser(String typeName);
    }


    public static interface ItemParser<T>{

        Optional<Item<T>> parseItem(String content);
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
            ItemParser<?> itemParser = itemTypeMapper.getParser(token.getTypeName());
            if(itemParser != null) {
                Optional<? extends Item<?>> item = itemParser.parseItem(token.getItemContent());
                if(item.isPresent()) {
                    result.add(item.get());
                }
            }
//        }
        return result;
    }
}

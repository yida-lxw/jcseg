package org.lionsoul.jcseg.analyzer.v5x;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.lionsoul.jcseg.tokenizer.core.ADictionary;
import org.lionsoul.jcseg.tokenizer.core.DictionaryFactory;
import org.lionsoul.jcseg.tokenizer.core.JcsegException;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;

/**
 * Created by Lanxiaowei
 */
public class JcsegTokenizerFactory extends TokenizerFactory 
{
    /**分词模式：complex、simple、detect、search*/
    private int mode;
    /**jcseg.properties属性文件的加载路径*/
    private String propertiesPath;
    /**Jcseg配置对象*/
    private JcsegTaskConfig config;
    /**Jcseg字典对象*/
    private ADictionary dic;

    public JcsegTokenizerFactory(Map<String, String> args) {
        super(args);
        String _mode = args.get("mode");
        if (_mode == null || "".equals(_mode)) {
            this.mode = JcsegTaskConfig.COMPLEX_MODE;
        } else {
            _mode = _mode.toLowerCase();
            if ("simple".equalsIgnoreCase(_mode) ) {
                mode = JcsegTaskConfig.SIMPLE_MODE;
            } else if ("detect".equalsIgnoreCase(_mode) ) {
                mode = JcsegTaskConfig.DETECT_MODE;
            } else if ("search".equalsIgnoreCase(_mode) ) {
                mode = JcsegTaskConfig.SEARCH_MODE;
            } else {
                mode = JcsegTaskConfig.COMPLEX_MODE;
            }
        }

        String propertiesFilePath = args.get("propertiesFilePath");
        if(null == propertiesFilePath || "".equals(propertiesFilePath)) {
            config = new JcsegTaskConfig(true);
            dic = DictionaryFactory.createSingletonDictionary(config);
        } else {
            this.propertiesPath = propertiesFilePath;
        }
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        try {
            if(null != config && null != dic) {
                return new JcsegTokenizer(mode, config, dic);
            }
            return new JcsegTokenizer(this.propertiesPath,this.mode);
        } catch (JcsegException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

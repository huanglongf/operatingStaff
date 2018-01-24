package com.gomeplus;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gomeplus.cache.GlobalCache;
import com.gomeplus.jdbc.ImpalaJdbc;
import com.gomeplus.utils.DateUtil;

@SpringBootApplication
@EnableAutoConfiguration
public class BootStrap {

	private static Logger logger = LoggerFactory.getLogger(BootStrap.class);

	private static String sql_path = null;// sql的xml文件所在的目录，可自由定义位置，将xml文件抽离到其他位置

	static {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 加载所有sql文件及配置文件
	 */
	private static void init() throws Exception {
		URL resource = BootStrap.class.getClassLoader().getResource("application.properties");
		Properties p = new Properties();
		InputStream istream = new FileInputStream(resource.getPath());
		p.load(istream);
		ImpalaJdbc.driver = (String) p.get("jdbc.driver");
		ImpalaJdbc.impala_url = (String) p.get("jdbc.url");
		sql_path = (String) p.get("sql.path");


		if (sql_path == null || "".equals(sql_path)) {
			logger.info("请配置sql_path参数，否则系统无法启动!");
			System.exit(0);
		} else {
			File file = new File(sql_path);
			File[] listFiles = file.listFiles();
			if (listFiles != null && listFiles.length > 0) {
				SAXReader reader = new SAXReader(false);
				for (int i = 0; i < listFiles.length; i++) {
					logger.info("加载sql文件:"+listFiles[i].getName());
					GlobalCache.SqlDocumentContainer.put(listFiles[i].getName(), reader.read(listFiles[i]));
				}
			}else{
				logger.info("未找到sql文件 位置，请检查sql_path参数!");
			}
		}
	}

	// 程序入口
	public static void main(String[] args) {
		 SpringApplication.run(BootStrap.class, args);
		 logger.info("运营参谋数据服务于:" + DateUtil.dateToMMddHHmmss() + "成功启动!");
	}
}

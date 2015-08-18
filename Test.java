import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONObject;

public class Test
{
  private static int TIME_WORK_BOT = 0;
  


  private static final java.util.Calendar TIME_END_WORK_BOT = new java.util.GregorianCalendar(2016, 7, 2, 1, 0, 0);
  

  private static String KEY_SECRET = "";
  
  private static final String INITIAL_SYMBOLS_KEY_SECRET = "sk8kE";
  
  private static final String FINAL_SYMBOLS_KEY_SECRET = "8drt0";
  private static final int DELAY_SEND_QUERY = 100;
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
  
  static
  {
    TIME_END_WORK_BOT.add(5, 1);
  }
  
  public static class StuffToTrading
  {
    private final String classId;
    private final String instanceId;
    private final String price;
    private final String hash;
    private final String nameStuff;
    private final String comment;
    private final String resultQueryForSend;
    
    StuffToTrading(String classId, String instanceId, String price, String hash, String nameStuff, String comment) throws IllegalArgumentException
    {
      this.classId = classId.replaceAll("\"", "");
      this.instanceId = instanceId;
      this.price = price;
      this.hash = hash;
      this.nameStuff = nameStuff;
      this.comment = comment.replaceAll("\"", "");
      
      if ((this.classId == null) || (this.instanceId == null) || (this.price == null) || (this.hash == null)) {
        throw new IllegalArgumentException("NULL в аргументах конструктора.");
      }
      try
      {
        Integer.parseInt(this.classId);
        Integer.parseInt(this.instanceId);
        Integer.parseInt(this.price);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Ошибка при приведении к числовому типу таких полей как classId, instanceId, price. classId = " + this.classId + ", instanceId = " + this.instanceId + ", price = " + this.price + ". Проверьте соответствующие данные и повторите попытку. " + "Данные значения должны быть ТОЛЬКО числового целого типа.");
      }
      



      resultQueryForSend = ("http://market.dota2.net/api/Buy/" + this.classId + "_" + this.instanceId + "/" + this.price + "/" + this.hash + "/?key=" + Test.KEY_SECRET);
    }
    
    public String getClassId()
    {
      return classId;
    }
    
    public String getInstanceId() {
      return instanceId;
    }
    
    public String getPrice() {
      return price;
    }
    
    public String getHash() {
      return hash;
    }
    
    public String getNameStuff() {
      return nameStuff;
    }
    
    public String getComment() {
      return comment;
    }
    
    public String getResultQueryForSend() {
      return resultQueryForSend;
    }
    
    public String toString()
    {
      System.out.println("classId ===== " + classId);
      return String.format("\"classId\":\"%s\", \"instanceId\":\"%s\", \"price\":\"%s\", \"hash\":\"%s\", \"nameStuff\":\"%s\", \"comment\":\"%s\"", new Object[] { classId, instanceId, price, hash, nameStuff, comment });
    }
  }
  
  public static List<Test.StuffToTrading> getStuffToTrading()
    throws Exception
  {
    BufferedReader fRead = null;
    try {
      fRead = new BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream("ItemsToBuy.data"), "windows-1251"));
    }
    catch (java.io.FileNotFoundException e) {
      System.out.println(DATE_FORMAT.format(new Date()) + ". Не удалось обнаружить файл ItemsToBuy.data, который " + "должен содержать информацию о скупаемых вещах на площадке market.");
      
      throw new Exception();
    } catch (Exception e) {
      System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка при открытии файл\tItemsToBuy.data, который " + "должен содержать информацию о скупаемых вещах на площадке market.");
      
      throw new Exception();
    }
    
    list = new java.util.ArrayList();
    int lineNumberInFileStaff = 0;
    try {
      while (fRead.ready()) {
        lineNumberInFileStaff++;
        

        String readStr = fRead.readLine().trim();
        if ((readStr.indexOf("//") != 0) && (!readStr.isEmpty()))
        {


          String[] paramsStuff = readStr.split("\" \"");
          
          if (paramsStuff.length != 6) {
            throw new IllegalArgumentException("Нарушена структура данных в файле ItemsToBuy.data, строка " + lineNumberInFileStaff + ". Проверьте правильность параметров в файле ItemsToBuy.data и повторите попытку снова.");
          }
          
          try
          {
            Test.StuffToTrading stuffToTrading = new Test.StuffToTrading(paramsStuff[0], paramsStuff[1], paramsStuff[2], paramsStuff[3], paramsStuff[4], paramsStuff[5]);
            
            list.add(stuffToTrading);
          } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage() + " Строка в файле " + lineNumberInFileStaff + ".");
          }
        }
      }
      













      return list;
    }
    catch (Exception e)
    {
      System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка чтения файла ItemsToBuy.data. " + e.getMessage());
      
      throw new Exception();
    } finally {
      try {
        if (fRead != null) {
          fRead.close();
        }
      } catch (IOException e) {
        throw new IOException(DATE_FORMAT.format(new Date()) + ". Ошибка закрытия файла ItemsToBuy.data.");
      }
    }
  }
  






  public static String getPage(String urlStr)
    throws IOException
  {
    java.net.URL url = new java.net.URL(urlStr);
    java.net.URLConnection connection = url.openConnection();
    BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream(), "Windows-1251"));
    StringBuilder lines = new StringBuilder();
    
    while ((line = reader.readLine()) != null) {
      lines.append(line).append('\n');
    }
    String line = null;
    return lines.toString();
  }
  









  public static String[] parseUI_BID(String responseServer)
  {
    String[] arrayUI_BID = responseServer.split("\"}],\"");
    for (int i = 0; i < arrayUI_BID.length; i++)
    {



      if (!arrayUI_BID[i].contains("\"ui_bid\":\"")) {
        arrayUI_BID[i] = "0";

      }
      else
      {
        int startSubstring = arrayUI_BID[i].indexOf("\"ui_bid\":\"") + 10;
        
        String newMessage = arrayUI_BID[i].substring(startSubstring, startSubstring + 20);
        int endBracket = newMessage.indexOf('"');
        arrayUI_BID[i] = newMessage.substring(0, endBracket);
      } }
    return arrayUI_BID;
  }
  

  public static void soundInformation()
  {
    try
    {
      javax.sound.midi.ShortMessage myMsg = new javax.sound.midi.ShortMessage();
      


      myMsg.setMessage(144, 4, 60, 93);
      javax.sound.midi.Synthesizer synth = javax.sound.midi.MidiSystem.getSynthesizer();
      javax.sound.midi.Receiver synthRcvr = synth.getReceiver();
      synth.open();
      synthRcvr.send(myMsg, -1L);
      Thread.sleep(500L);
      synthRcvr.send(myMsg, -1L);
      Thread.sleep(500L);
      synthRcvr.send(myMsg, -1L);
      Thread.sleep(500L);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static String loadSecretKey() throws Exception {
    String secretKey = null;
    try {
      java.io.InputStreamReader fisProperties = null;
      java.util.Properties fProperties = null;
      propertiesFileName = "SECRET_KEY.properties";
      try {
        fProperties = new java.util.Properties();
        fisProperties = new java.io.InputStreamReader(new java.io.FileInputStream(propertiesFileName), "windows-1251");
        fProperties.load(fisProperties);
        secretKey = (String)fProperties.get("SecretKeyMarket");
        







        if (fisProperties != null) {
          try {
            fisProperties.close();
          } catch (Exception e) {
            System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка при закрытии потока данных для файла " + propertiesFileName + ".");
          }
        }
        


        if (secretKey != null) {
          break label402;
        }
      }
      catch (java.io.FileNotFoundException e)
      {
        System.out.println(DATE_FORMAT.format(new Date()) + ". Не удалось найти файл " + propertiesFileName + " с секретным ключом. Бот не запущен.");
      }
      catch (Exception e) {
        System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка при обработке файл " + propertiesFileName + " с секретным ключом. Бот не запущен.");
        
        throw new Exception(e);
      } finally {
        if (fisProperties != null) {
          try {
            fisProperties.close();
          } catch (Exception e) {
            System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка при закрытии потока данных для файла " + propertiesFileName + ".");
          }
        }
      }
      


      System.out.println(DATE_FORMAT.format(new Date()) + ". Не удалось найти параметр SecretKeyMarket в файле " + propertiesFileName + " который должен содержать ваш секретный ключ. Бот не запущен.");
      
      throw new Exception();
    }
    catch (Exception e)
    {
      String propertiesFileName;
      




















      label402:
      



















      boolean resultValidateSecretKeyMarket;
      



















      throw new Exception();
    }
    if (secretKey.equals("ВАШ_СЕКРЕТНЫЙ_КЛЮЧ")) {
      System.out.println(DATE_FORMAT.format(new Date()) + ". В файле " + propertiesFileName + " в параметре " + "SecretKeyMarket вместо параметра ВАШ_СЕКРЕТНЫЙ_КЛЮЧ должен быть введен ваше секретный ключ, который должен быть такого формата: " + "sk8kE" + "*********************" + "8drt0");
      

      throw new Exception(); }
    if (secretKey.isEmpty()) {
      System.out.println(DATE_FORMAT.format(new Date()) + ". В файле " + propertiesFileName + " в параметре " + "SecretKeyMarket должен быть введен ваше секретный ключ, который должен быть такого формата: " + "sk8kE" + "*********************" + "8drt0");
      

      throw new Exception();
    }
    System.out.println(DATE_FORMAT.format(new Date()) + ". Проверка секретного ключа на валидность для бота. Ключ должен соответствовать шаблону: " + "sk8kE" + "*********************" + "8drt0");
    
    if ((secretKey.indexOf("sk8kE") == 0) && (secretKey.indexOf("8drt0") == secretKey.length() - 5))
    {
      System.out.println(DATE_FORMAT.format(new Date()) + ". Секретный ключ успешно прошел проверку бота!");
    } else {
      System.out.println(DATE_FORMAT.format(new Date()) + ". Секретный ключ не прошел проверку валидности для бота. " + " Напоминаем о том, что данная версия бота готовится и высылается конктерно для вас, где " + "привязка происходит непосредственно к вашему аккаунту и лишь по вашему уникальному секретному ключу. Бот не запущен.");
      

      throw new Exception();
    }
    

    System.out.println(DATE_FORMAT.format(new Date()) + ". Проверка секретного ключа на валидность для http://market.dota2.net/.");
    resultValidateSecretKeyMarket = false;
    try {
      String requestForValidateSecretKey = "http://market.dota2.net/api/Trades/?key=" + secretKey;
      String response = getPage(requestForValidateSecretKey);
      try
      {
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        Object obj = parser.parse(response);
        





        if ((obj instanceof JSONObject))
        {
          JSONObject jsonObj = (JSONObject)obj;
          if (jsonObj.containsKey("error")) {
            String errorMessage = (String)jsonObj.get("error");
            
            System.out.println(DATE_FORMAT.format(new Date()) + ". Не удалось выполнить авторизацию на https://market.dota2.net.tm. Причина: " + errorMessage);
            
            System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка при обработке файла настроек Settings.properties. Бот не запущен.");
          }
        } else {
          System.out.println(DATE_FORMAT.format(new Date()) + ". Секретный ключ успешно прошел проверку https://market.dota2.net.tm!");
          System.out.println(DATE_FORMAT.format(new Date()) + ". БОТ ГОТОВ К РАБОТЕ!");
          resultValidateSecretKeyMarket = true;
        }
      } catch (org.json.simple.parser.ParseException e) {
        System.out.println(DATE_FORMAT.format(new Date()) + ". Произошла ошибка во время парсинга полученного JSON'a от сервера.");
        System.out.println(DATE_FORMAT.format(new Date()) + ". Ответ полученный от сервера: " + response);
      }
      
      if (!resultValidateSecretKeyMarket) {
        throw new Exception();
      }
    } catch (Exception e) {
      throw new Exception();
    }
    




    return secretKey;
  }
  
  public static void buyWhileDowntime(List listStuff, int downtime) throws InterruptedException {
    long timeFinish = System.currentTimeMillis() + downtime;
    
    while (System.currentTimeMillis() < timeFinish) {
      for (int i = 0; i < listStuff.size(); i++) {
        String response = "";
        try {
          response = getPage(((Test.StuffToTrading)listStuff.get(i)).getResultQueryForSend());
          if (response.contains("ok")) {
            System.out.println(DATE_FORMAT.format(new Date()) + ". Приобретен " + ((Test.StuffToTrading)listStuff.get(i)).getNameStuff() + ".");
          }
        }
        catch (Exception e) {
          System.out.println(DATE_FORMAT.format(new Date()) + ". Произошла ошибка покупки предмета, запрос" + " отправлен, предмет не куплен. Причина: " + e.getMessage());
        }
      }
    }
  }
  
  public static void main(String[] args)
  {
    int cooldownCheckTime = 7200000;
    long checkTimeWorkingBot = System.currentTimeMillis() + cooldownCheckTime;
    long currentTimeToServer = 0L;
    try {
      try {
        String timeStr = getPage("http://www.direct-time.ru/track.php?id=time_utc");
        timeStr = timeStr.trim();
        
        currentTimeToServer = Long.parseLong(timeStr) + 3600000L;
      } catch (Exception e) {
        System.out.println(DATE_FORMAT.format(new Date()) + ". Не удалось получить и обработать ответ от сервера по текущему времени.");
        throw new Exception();
      }
      
      System.out.println("Доброго времени суток! Время согласноhttp://www.direct-time.ru: " + DATE_FORMAT.format(new Date(currentTimeToServer)));
      System.out.println("Время завершения подписки на бота: " + DATE_FORMAT.format(TIME_END_WORK_BOT.getTime()));
      
      if (currentTimeToServer > TIME_END_WORK_BOT.getTime().getTime()) {
        System.out.println(DATE_FORMAT.format(new Date()) + ". Для работы бота продлите пожалуйста подписку, подробнее на BotForCS@mail.ru" + "по боту для CSGO или для бота по Dota2 наBotForDota2@mail.ru.");
        


        throw new Exception();
      }
    } catch (Exception e) {
      System.out.print(DATE_FORMAT.format(new Date()) + ". Бот завершает свою работу. Для продолжения нажмите клавишу Enter...");
      try
      {
        System.in.read();
      } catch (IOException localIOException) {}
      System.exit(0);
    }
    try
    {
      KEY_SECRET = loadSecretKey();
    } catch (Exception e) {
      System.out.print(DATE_FORMAT.format(new Date()) + ". Бот завершает свою работу. Для продолжения нажмите клавишу Enter...");
      try {
        System.in.read();
      } catch (IOException localIOException1) {}
      System.exit(0);
    }
    
    System.out.print(DATE_FORMAT.format(new Date()) + ". Введите время работы бота в минутах: ");
    try {
      BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
      TIME_WORK_BOT = Integer.parseInt(reader.readLine()) * 1000 * 60;
    } catch (Exception e) {
      System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка при вводе некорректных данных, перезапустите бота и повторите попытку.");
      System.out.print(DATE_FORMAT.format(new Date()) + ". Бот завершает свою работу. Для продолжения нажмите клавишу Enter...");
      try {
        System.in.read();
      } catch (IOException localIOException2) {}
      System.exit(0);
    }
    
    long timeFinishLong = System.currentTimeMillis() + TIME_WORK_BOT;
    
    List<Test.StuffToTrading> list = null;
    try {
      list = getStuffToTrading();
    } catch (Exception e) {
      System.out.print(DATE_FORMAT.format(new Date()) + ". Бот завершает свою работу. Для продолжения нажмите клавишу Enter...");
      try {
        System.in.read();
      } catch (IOException localIOException3) {}
      System.exit(0);
    }
    
    System.out.println(DATE_FORMAT.format(new Date()) + ". Началась отправка запросов серверу на скупку указанных вещей...");
    try {
      while (System.currentTimeMillis() < timeFinishLong) {
        for (int i = 0; i < list.size(); i++) {
          try {
            String response = getPage(((Test.StuffToTrading)list.get(i)).getResultQueryForSend());
            if (response.contains("ok")) {
              System.out.println(DATE_FORMAT.format(new Date()) + ". Приобретен " + ((Test.StuffToTrading)list.get(i)).getNameStuff() + ".");
            }
          } catch (Exception e) {
            System.out.println(DATE_FORMAT.format(new Date()) + ". Произошла ошибка при отправке запроса на скупку предмета: " + ((Test.StuffToTrading)list.get(i)).getNameStuff() + ". Продалжаем отправлять запросы... ");
          }
          
          Thread.sleep(100L);
        }
        
        try
        {
          String buy = getPage("http://market.dota2.net/api/Trades/?key=" + KEY_SECRET);
          

          if (buy.contains("\"ui_status\":\"4\"")) {
            System.out.println(DATE_FORMAT.format(new Date()) + ". Необходимо забрать вещь у бота!");
            
            String[] arrayUI_BID = parseUI_BID(buy);
            
            for (int i = 0; i < arrayUI_BID.length; i++) {
              try {
                long ui_bidLong = Long.parseLong(arrayUI_BID[i]);
                


                if ((ui_bidLong != 0L) && (ui_bidLong != 1L))
                {

                  String resultMessageForSend = "http://market.dota2.net/api/ItemRequest/out/" + ui_bidLong + "/?key=" + KEY_SECRET;
                  
                  System.out.println(DATE_FORMAT.format(new Date()) + ". Формируем и отправляем запрос для получения стим-обмена с ботам маркета.");
                  
                  try
                  {
                    String answerServerForSwapToBot = getPage(resultMessageForSend);
                    boolean success = false;
                    JSONObject jsonObj = null;
                    try {
                      org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
                      Object obj = parser.parse(answerServerForSwapToBot);
                      jsonObj = (JSONObject)obj;
                      success = ((Boolean)jsonObj.get("success")).booleanValue();
                    } catch (Exception e) {
                      System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка при обработке JSON'a." + " Ответ полученный от сервера: " + answerServerForSwapToBot);
                    }
                    

                    if (success) {
                      System.out.println("\n" + DATE_FORMAT.format(new Date()) + ". Результат: true" + ", никнэйм бота: " + jsonObj.get("nick"));
                      
                      System.out.println(DATE_FORMAT.format(new Date()) + ". У вас есть 1 минута что " + "бы забрать вещь у бота. Если этого не сделать в течении 10 минут, " + "то новые вещи не будет скупаться вообще до тех пор, пока будут активные трейды.");
                      

                      soundInformation();
                      buyWhileDowntime(list, 60000);
                    }
                    else
                    {
                      String errorMessage = (String)jsonObj.get("error");
                      errorMessage = errorMessage.replaceAll("\\\\", "\\");
                      System.out.print("\n" + DATE_FORMAT.format(new Date()) + ". Результат: false, по причине: " + errorMessage);
                    }
                  }
                  catch (Exception e) {
                    System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка при получении ответа от " + "сервера по стим обмену с ботам. Продолжаем отправлять запросы... ");
                  }
                }
              }
              catch (NumberFormatException eNum) {
                System.out.println(DATE_FORMAT.format(new Date()) + ". Ошибка обработки полученного ui_bid. ui_bid = " + arrayUI_BID[i] + ", i = " + i);
              }
            }
          }
        }
        catch (Exception e) {
          System.out.println(DATE_FORMAT.format(new Date()) + ". Произошла ошибка при отправке запроса на " + "получение списка инвентаря, что бы проверить, есть ли вещи, которые нужно забрать у бота." + "Продалжаем отправлять запросы... ");
        }
        


        if (System.currentTimeMillis() > checkTimeWorkingBot) {
          if (currentTimeToServer + cooldownCheckTime > TIME_END_WORK_BOT.getTime().getTime()) {
            System.out.println(DATE_FORMAT.format(new Date()) + ". На данный момент подписка на бота закончилась, для работы бота продлите пожалуйста подписку. Подробнее на BotForCS@mail.ru " + "по боту для CSGO или на BotForDota2@mail.ru - для бота Dota2");
            
            System.out.print(DATE_FORMAT.format(new Date()) + ". Бот завершает свою работу. Для продолжения нажмите клавишу Enter...");
            try {
              System.in.read();
            } catch (IOException localIOException4) {}
            System.exit(0);
          } else {
            currentTimeToServer += cooldownCheckTime;
            checkTimeWorkingBot += cooldownCheckTime;
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public Test() {}
}

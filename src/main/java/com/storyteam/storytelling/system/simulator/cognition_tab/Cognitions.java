package com.storyteam.storytelling.system.simulator.cognition_tab;

import com.storyteam.storytelling.system.simulator.cognition_tab.utils.CognitionTierInfo;
import com.storyteam.storytelling.system.simulator.me_tab.attributes.data.AttributeType;

public class Cognitions {
   public static final Cognition REFLECTION = Cognition.builder("reflection")
      .title("Рефлексия")
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит вам чаще задумываться о концептах мироздания и извлекать из своих наблюдений пользу. Вероятность получить слово из взаимодействий с окружающим миром растёт на 10%."
            )
            .level(1)
            .eidos(10)
            .type(AttributeType.MIND, 1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Вероятность получить слово из собственных размышлений растёт на 10%.")
            .level(2)
            .eidos(15)
            .type(AttributeType.MIND, 3)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Вероятность получить слово из диалогов с другими игроками растёт на 10%.")
            .level(4)
            .eidos(35)
            .type(AttributeType.MIND, 5)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("При получении уровня можно выбрать дополнительное слово на обдумывание.")
            .level(10)
            .eidos(200)
            .type(AttributeType.MIND, 10)
            .build()
      )
      .build();
   public static final Cognition CITY_HALL = Cognition.builder("city_hall")
      .title("Городская ратуша")
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки городской ратуши. Это здание является центром вашего поселения формиксов и служит точкой сбора ресурсов и найма рабочих. Вы получаете возможность построить Ратушу. Разблокирует улучшение постройки до 3 уровня."
            )
            .level(1)
            .eidos(10)
            .type(AttributeType.WILLPOWER, 1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder().desctiption("Разблокирует улучшение постройки до 5 уровня.").level(5).eidos(100).type(AttributeType.WILLPOWER, 6).build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особую способность постройки. (см. в ртске городская ратуша)")
            .level(10)
            .eidos(350)
            .type(AttributeType.WILLPOWER, 14)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня, а также открывает особую способность. (см. в ртске городская ратуша).")
            .level(15)
            .eidos(1000)
            .type(AttributeType.WILLPOWER, 21)
            .build()
      )
      .build();
   public static final Cognition RICE_FIELD = Cognition.builder("rice_field")
      .title("Рисовое поле")
      .dependencies(CITY_HALL)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность создавать и улучшать рисовое поле, снабжающее ваших формиксов едой. Открывает собственно постройку рисофермы."
            )
            .level(2)
            .eidos(20)
            .type(AttributeType.MIND, 2)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует улучшение постройки до 5 уровня.").level(5).eidos(50).type(AttributeType.MIND, 5).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует активную особую способность постройки. (смотри в ртс: рисовое поле в ртс).")
            .level(7)
            .eidos(140)
            .type(AttributeType.MIND, 10)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня, а также открывает пассивную особую способность.")
            .level(10)
            .eidos(300)
            .type(AttributeType.MIND, 13)
            .build()
      )
      .build();
   public static final Cognition WORKER = Cognition.builder("worker")
      .title("Рабочий")
      .dependencies(CITY_HALL)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля найм рабочих в городской ратуше. Рабочие – универсальные формиксы, способные к выполнению широкого спектра задач. Разблокирует возможность изучить и создавать юнит."
            )
            .level(1)
            .eidos(1)
            .type(AttributeType.BODY, 1)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(5).eidos(10).type(AttributeType.BODY, 3).build())
      .level(3, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(8).eidos(55).type(AttributeType.BODY, 10).build())
      .level(4, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(13).eidos(130).type(AttributeType.BODY, 13).build())
      .build();
   public static final Cognition SPECIALIZATION_FARMER = Cognition.builder("specialization_farmer")
      .title("Специализация: фермер")
      .dependencies(WORKER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит рабочим формиксам стать фермерами. Фермеры эффективнее работают в поле. Открывает возможность смены специализации работяги на фермера."
            )
            .level(3)
            .eidos(10)
            .type(AttributeType.MIND, 4)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(4).eidos(30).type(AttributeType.MIND, 4).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита и открывает активное особое умение.")
            .level(5)
            .eidos(100)
            .type(AttributeType.MIND, 8)
            .build()
      )
      .level(4, CognitionTierInfo.builder().desctiption("Открывает пассивное особое умение юнита.").level(10).eidos(150).type(AttributeType.MIND, 14).build())
      .build();
   public static final Cognition COMMUNICATION_TOWER = Cognition.builder("communication_tower")
      .title("Вышка")
      .dependencies(CITY_HALL)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки вышки связи. Вышка связи нужна для дистанционного общения с другими разумными существами. Даёт возможность изучить и построить вышку."
            )
            .level(4)
            .eidos(20)
            .type(AttributeType.MIND, 2)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Перехватываются более объёмные сообщения, в них меньше потерянных фрагментов.")
            .level(10)
            .eidos(150)
            .type(AttributeType.MIND, 12)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Открывает возможность обмениваться сообщениями с др игроками.")
            .level(15)
            .eidos(340)
            .type(AttributeType.MIND, 22)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует дальнейшее улучшение постройки до 10 уровня.")
            .level(2)
            .eidos(20)
            .type(AttributeType.MIND, 2)
            .build()
      )
      .build();
   public static final Cognition WAREHOUSE = Cognition.builder("warehouse")
      .title("Склад")
      .dependencies(RICE_FIELD)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки склада. Склад – место для хранения различных предметов, а также ваших ресурсов. Увеличивает максимальный запас ресурсов. Открывает возможность постройки склада."
            )
            .level(2)
            .eidos(5)
            .type(AttributeType.MIND, 1)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует улучшение постройки до 5 уровня.").level(4).eidos(20).type(AttributeType.MIND, 3).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые способности постройки. Позволяет улучшить до 6 уровня.")
            .level(8)
            .eidos(100)
            .type(AttributeType.MIND, 10)
            .build()
      )
      .level(
         4, CognitionTierInfo.builder().desctiption("Разблокирует улучшение постройки до 10 уровня.").level(10).eidos(150).type(AttributeType.MIND, 14).build()
      )
      .build();
   public static final Cognition WAREHOUSE_BRANCH = Cognition.builder("warehouse_branch")
      .title("Филиал склада")
      .dependencies(WAREHOUSE)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки филиала склада. Это здание напрямую связано со складом и нужно для удобства доставки ресурсов напрямую на склад из отдалённых областей. Открывает возможность постройки филиала склада."
            )
            .level(3)
            .eidos(5)
            .type(AttributeType.MIND, 2)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует особую способность постройки.").level(4).eidos(20).type(AttributeType.MIND, 5).build())
      .level(3, CognitionTierInfo.builder().desctiption("Улучшает особую способность постройки.").level(8).eidos(80).type(AttributeType.MIND, 15).build())
      .level(4, CognitionTierInfo.builder().desctiption("Улучшает особую способность постройки.").level(10).eidos(110).type(AttributeType.MIND, 20).build())
      .build();
   public static final Cognition EDROV_GLACIER = Cognition.builder("edrov_glacier")
      .title("Эдровый ледник")
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Эдрового ледника – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(10)
            .eidos(100)
            .type(AttributeType.MIND, 11)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует месторождения в регионе.").level(-1).eidos(-1).type(AttributeType.MIND, -1).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition CALCULATIVENESS = Cognition.builder("calculativeness")
      .title("Расчётливость")
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption("Познание этого навыка позволит вам управлять своим “Я” чуть лучше. Увеличивает количество эйдосов при отклонении мыслей на 15.")
            .level(5)
            .eidos(50)
            .type(AttributeType.WILLPOWER, 5)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Увеличивает количество эйдосов при отклонении мыслей на 100.")
            .level(15)
            .eidos(200)
            .type(AttributeType.WILLPOWER, 6)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Теперь за принятие мыслей вы получаете 100 эйдосов.")
            .level(20)
            .eidos(850)
            .type(AttributeType.WILLPOWER, 30)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Каждая новая мысль при принятии или отклонении приносит дополнительные 200 эйдосов.")
            .level(40)
            .eidos(5200)
            .type(AttributeType.WILLPOWER, 60)
            .build()
      )
      .build();
   public static final Cognition BARRACKS = Cognition.builder("barracks")
      .title("Казармы")
      .dependencies(CITY_HALL)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки казарм. Казармы служат для найма боевых формиксов в ваше войско. Открывает возможность постройки казарм. Можно прокачать до 3 лвла."
            )
            .level(2)
            .eidos(10)
            .type(AttributeType.BODY, 2)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует улучшение постройки до 5 уровня.").level(5).eidos(50).type(AttributeType.BODY, 5).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Повышает жизненные показатели всех боевых юнитов, нанимаемых в казармах, на 20%.")
            .level(10)
            .eidos(100)
            .type(AttributeType.BODY, 10)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня, а также открывает пассивную особую способность. (см. казармы в ртс).")
            .level(20)
            .eidos(200)
            .type(AttributeType.BODY, 20)
            .build()
      )
      .build();
   public static final Cognition RECRUIT = Cognition.builder("recruit")
      .title("Рекрут")
      .dependencies(BARRACKS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность найма рекрутов в казармах. Рекруты – слабые, но универсальные боевые юниты, способные изучить разные стили боя. Открывает возможность создания рекрутов."
            )
            .level(4)
            .eidos(10)
            .type(AttributeType.WILLPOWER, 4)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(5).eidos(15).type(AttributeType.WILLPOWER, 5).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита и открывает активное особое умение.")
            .level(8)
            .eidos(30)
            .type(AttributeType.WILLPOWER, 10)
            .build()
      )
      .level(
         4, CognitionTierInfo.builder().desctiption("Открывает пассивное особое умение юнита.").level(10).eidos(40).type(AttributeType.WILLPOWER, 12).build()
      )
      .build();
   public static final Cognition SPECIALIZATION_BERSERKER = Cognition.builder("specialization_berserker")
      .title("Специализация: Берсерк")
      .dependencies(RECRUIT)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность для рекрута выучить стиль боя берсерка. Берсерки – отчаянные воины. Они полагаются на грубую силу, совершенно забывая о защите. Открывает возможность рекруту сменить специализацию на берсерка."
            )
            .level(4)
            .eidos(10)
            .type(AttributeType.WILLPOWER, 4)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(6).eidos(25).type(AttributeType.WILLPOWER, 7).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита и открывает пассивное особое умение.")
            .level(10)
            .eidos(100)
            .type(AttributeType.WILLPOWER, 17)
            .build()
      )
      .level(
         4, CognitionTierInfo.builder().desctiption("Открывает активное особое умение юнита.").level(15).eidos(230).type(AttributeType.WILLPOWER, 24).build()
      )
      .build();
   public static final Cognition ARCHER = Cognition.builder("archer")
      .title("Лучник")
      .dependencies(BARRACKS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность нанимать лучников в казармах. Лучники – медленные и уязвимые воины дальнего боя, поражающие цели на дистанции. Позволяет нанимать в казармах лучников."
            )
            .level(4)
            .eidos(10)
            .type(AttributeType.WILLPOWER, 4)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(5).eidos(15).type(AttributeType.WILLPOWER, 5).build())
      .level(3, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(8).eidos(30).type(AttributeType.WILLPOWER, 10).build())
      .level(
         4, CognitionTierInfo.builder().desctiption("Открывает активное особое умение юнита.").level(10).eidos(40).type(AttributeType.WILLPOWER, 12).build()
      )
      .build();
   public static final Cognition SPECIALIZATION_SPEARMAN = Cognition.builder("specialization_spearman")
      .title("Специализация: копейщик")
      .dependencies(RECRUIT)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность для рекрута выучить стиль боя копейщика. Копейщики – медленные воины, сражающиеся на средней дистанции. Открывает возможность рекруту сменить специализацию на копейщика."
            )
            .level(4)
            .eidos(10)
            .type(AttributeType.WILLPOWER, 4)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(6).eidos(25).type(AttributeType.WILLPOWER, 7).build())
      .level(3, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(10).eidos(100).type(AttributeType.WILLPOWER, 17).build())
      .level(
         4, CognitionTierInfo.builder().desctiption("Открывает активное особое умение юнита.").level(15).eidos(230).type(AttributeType.WILLPOWER, 24).build()
      )
      .build();
   public static final Cognition EPILITH_CANYON = Cognition.builder("epilith_canyon")
      .title("Каньон эпилитов")
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Каньона эпилитов  – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует месторождения в регионе.").level(-1).eidos(-1).type(AttributeType.WILLPOWER, -1).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition HEAD_ON_SHOULDERS = Cognition.builder("head_on_shoulders")
      .title("Голова на плечах")
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволяет вам ускорить мыслительный процесс, направляя спутанные логические цепочки в нужное русло. Слова обдумываются на 10% быстрее."
            )
            .level(5)
            .eidos(15)
            .type(AttributeType.BODY, 5)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Слова обдумываются на 15% быстрее.").level(6).eidos(25).type(AttributeType.WILLPOWER, 7).build())
      .level(3, CognitionTierInfo.builder().desctiption("Слова обдумываются на 20% быстрее.").level(6).eidos(25).type(AttributeType.BODY, 7).build())
      .level(4, CognitionTierInfo.builder().desctiption("Слова обдумываются на 50% быстрее.").level(6).eidos(25).type(AttributeType.BODY, 7).build())
      .build();
   public static final Cognition WALL = Cognition.builder("wall")
      .title("Стена")
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки стен. Стены – оборонительные сооружения, скрывающие ваши тёмные дела от посторонних глаз и катапульт. Открывает возможность постройки стен. Открывает улучшение стены до 3 лвла."
            )
            .level(2)
            .eidos(10)
            .type(AttributeType.BODY, 2)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует улучшение постройки до 5 уровня.").level(4).eidos(50).type(AttributeType.BODY, 5).build())
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует пассивную особую способность постройки.")
            .level(6)
            .eidos(100)
            .type(AttributeType.BODY, 10)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня, а также открывает пассивную особую способность.")
            .level(10)
            .eidos(320)
            .type(AttributeType.BODY, 14)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_FISHERMAN = Cognition.builder("specialization_fisherman")
      .title("Специализация: рыбак")
      .dependencies(WORKER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит рабочим формиксам стать рыбаками. Рыбаки куда эффективнее ловят рыбу и прочие дары глубин. Работягу можно перепрофилировать в рыбака."
            )
            .level(3)
            .eidos(10)
            .type(AttributeType.BODY, 4)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("Разблокирует повышение уровня юнита.").level(4).eidos(25).type(AttributeType.BODY, 6).build())
      .level(3, CognitionTierInfo.builder().desctiption("Открывает пассивное особое умение.").level(6).eidos(55).type(AttributeType.BODY, 8).build())
      .level(4, CognitionTierInfo.builder().desctiption("Открывает активное особое умение.").level(11).eidos(130).type(AttributeType.BODY, 12).build())
      .build();
   public static final Cognition DIPLOMACY = Cognition.builder("diplomacy")
      .title("Дипломатия")
      .dependencies(HEAD_ON_SHOULDERS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит вам лучше понимать как устроена коммуникация между живыми существами, что даст вам преимущество в социальных интеракциях. На 10% выше вероятность того, что удастся начать диалог с недружелюбным существом."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Требуется меньше действий для того, чтобы загладить вину перед другим существом.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Ваши слова действуют более успокаивающе на недовольных существ.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Вероятность успешного разрешения любой конфликтной ситуации увеличивается на 40%.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition PROFIT_SEEKER = Cognition.builder("profit_seeker")
      .title("Искатель выгоды")
      .dependencies(CALCULATIVENESS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит вам лучше понимать природу особых событий, происходящих у вас в мире. Еженедельные события, дающие положительный эффект, приносят на 10% больше выгоды."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Еженедельные события, уменьшающие население, забирают на 20% меньше юнитов.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Еженедельные события, связанные с добычей ресурсов, дают на 10% больше ресурсов.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Каждые четыре недели вы можете один раз уклониться от еженедельного события и избавить ваш мир от его эффекта.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition WILLPOWERFUL_DECISION = Cognition.builder("willful_decision")
      .title("Волевое решение")
      .dependencies(DIPLOMACY, PROFIT_SEEKER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка научит вас интуитивному пониманию концептов при разговоре с другими существами, что существенно облегчит возможность сложной коммуникации. Требуется на 1 очко воли меньше, чтобы сделать выбор в диалоге, если ваш предыдущий выбор был сделан в пользу этого атрибута."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Раз в день требуется на 1 очко воли меньше, чтобы сделать выбор в диалоге.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Раз в диалог требуется на 1 очко воли меньше, чтобы сделать выбор в диалоге.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Требуется на 1 очко воли меньше, чтобы сделать выбор в любом диалоге.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition CARPENTRY_WORKSHOP = Cognition.builder("carpentry_workshop")
      .title("Плотницкая мастерская")
      .dependencies(WORKER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки плотницкой мастерской. Эта постройка позволяет формиксам создавать различные простые изделия из версиформа, такие как мебель, сёдла и луки. Открывает возможность постройки плотницкой мастерской."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 3 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 6 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition PIER = Cognition.builder("pier")
      .title("Пирс")
      .dependencies(CARPENTRY_WORKSHOP)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность создавать и улучшать Пирс. Данная постройка служит для удобной ловли рыбы, а также может производить водные суда. Можно построить пирс."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 3 уровня. Позволяет производить Корыто.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 6 уровня, а также открывает пассивную особую способность постройки.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня, а также открывает особое исследование.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition TEMPLE = Cognition.builder("temple")
      .title("Храм")
      .dependencies(DIPLOMACY)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки храма. Это здание позволяет нанимать формиксов-монахов, а также распространяет благословение безымянного бога в области вокруг. Можно построить храм."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует активную особую способность постройки.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует активную особую способность постройки, а также открывает особую пассивную способность постройки.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует активную особую способность постройки, а также открывает особую пассивную способность постройки.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition INSIGHTFULNESS = Cognition.builder("insightfulness")
      .title("Интуиция")
      .dependencies(REFLECTION)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит вам лучше понимать мимику и интонации других существ. Вы различаете эмоции других существ на 10% чаще."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Другие существа чаще пишут вам сообщения.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Вы различаете эмоции других существ на 20% чаще.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption(
               "В диалоге вы можете получить подсказку о наиболее благоприятном для вас выборе. Действует всего один раз в диалоге с каждым из существ."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition LIBRARY = Cognition.builder("library")
      .title("Библиотека")
      .dependencies(INSIGHTFULNESS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки библиотеки. Библиотека – особая постройка, позволяющая формиксам получать новые знания о мире. Можно построить библиотеку."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 3 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 5 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition FLYING_MACHINE = Cognition.builder("flying_machine")
      .title("Летательный аппарат")
      .dependencies(LIBRARY)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность производства летательного аппарата в городской ратуше. Летательные аппараты служат для быстрого перемещения по миру. Можно построить летательный аппарат."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Переоборудует летательный аппарат в более эффективный.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня аппарата и открывает пассивное особое умение.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Переоборудует летательный аппарат в более эффективный.  Разблокирует повышение уровня аппарата.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition PORTAL = Cognition.builder("portal")
      .title("Портал")
      .dependencies(TEMPLE)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки Портала. Это здание служит для перемещения формиксов и других существ между порталами, и, вероятно, даже между мирами. Открывает постройку портала для юнитов."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует постройку портала для владельца мира. Позволяет отправляться в другие миры.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение Порталов до 5 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует производство портативного телепортатора, а также открывает улучшение Порталов до 10 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition TOWER = Cognition.builder("tower")
      .title("Башня")
      .dependencies(WALL)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки Башни. Эта постройка позволяет лучникам стрелять дальше и быть неуязвимыми для атак. Можно построить башню. Можно улучшить до 3 лвла."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 5 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует пассивную особую способность постройки.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition FORGE = Cognition.builder("forge")
      .title("Кузница")
      .dependencies(BARRACKS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки кузницы. Эта постройка позволяет формиксам создавать различные продвинутые изделия из версиформа, такие как вооружение, броня и инструменты. Открывает возможность постройки кузницы."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 3 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 6 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня, а также разблокирует особое исследование.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition RAM = Cognition.builder("ram")
      .title("Таран")
      .dependencies(FORGE)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность производства тарана в оружейной мастерской. Таран – простое осадное орудие, необходимое для разрушения стен и укреплений врагов. Можно исследовать таран. Спавнится в оружейной мастерской."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Открывает пассивное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition CATAPULT = Cognition.builder("catapult")
      .title("Катапульта")
      .dependencies(RAM)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность производства катапульты в оружейной мастерской. Катапульта – осадное орудие дистанционного поражения, необходимое для разрушения зданий врагов. Можно исследовать катапульту. Спавнится в оружейной мастерской."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_ETHER_COLLECTOR = Cognition.builder("specialization_ether_collector")
      .title("Специализация: Эфиросборщик")
      .dependencies(WORKER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит рабочим формиксам стать эфирными сборщиками. Эфирные сборщики легче собирают эйдосы из гейзеров. Открывает возможность смены специализации работяги на эфирного сборщика."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_MINER = Cognition.builder("specialization_miner")
      .title("Специализация: рудокоп")
      .dependencies(WORKER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит рабочим формиксам стать рудокопами. Рудокопы эффективнее выкапывают залежи версиформа. Открывает возможность смены специализации работяги на рудокопа (сборщика версиформа)."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition AUTO_COLLECTOR = Cognition.builder("auto_collector")
      .title("Автосборщик")
      .dependencies(SPECIALIZATION_ETHER_COLLECTOR, SPECIALIZATION_MINER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки различных вариантов Автосборщиков. Эти здания позволяют рабочим формиксам взаимодействовать с месторождениями ресурсов и автоматически доставляют их на склад. Разблокирует возможность постройки Эйдосного фильтра."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует возможность постройки Эфирной скважины.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует возможность постройки Версиформового карьера.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает особые пассивные умения у всех видов Автосборщиков.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition BOAT = Cognition.builder("boat")
      .title("Лодка")
      .dependencies(PIER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность производства лодок на пирсе. Лодки – водный транспорт для формиксов и владельца мира. Есть возможность исследовать лодку. Спавнится на пирсе."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition LONGSHIP = Cognition.builder("longship")
      .title("Ладья")
      .dependencies(BOAT)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность производства ладей на пирсе. Ладьи - боевые корабли, служащие для поражения вражеских целей с моря. Есть возможность создавать ладьи. Это боевой корабль."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition TWILIGHT_GRAVEYARD = Cognition.builder("twilight_graveyard")
      .title("Сумеречный могильник")
      .dependencies(EPILITH_CANYON)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Сумеречного могильника – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition MONUMENTAL_VALLEY = Cognition.builder("monumental_valley")
      .title("Монументальная долина")
      .dependencies(TWILIGHT_GRAVEYARD)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Монументальной долины – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition MYCOMUNDUS_VULGARIS = Cognition.builder("mycomundus_vulgaris")
      .title("Микомундус Вульгарис")
      .dependencies(MONUMENTAL_VALLEY)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Микомундус Вульгарис – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition VICTORIA_REGIA = Cognition.builder("victoria_regia")
      .title("Виктория Регия")
      .dependencies(MYCOMUNDUS_VULGARIS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Виктории Регии – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition CRIMSON_ULCERS = Cognition.builder("crimson_ulcers")
      .title("Багряные язвы")
      .dependencies(VICTORIA_REGIA)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Багряных язв – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition BALANCED_CHOICE = Cognition.builder("balanced_choice")
      .title("Взвешенный выбор")
      .dependencies(CALCULATIVENESS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит ускорить развитие путём более глубокого восприятия концептов мироздания. Появляется вероятность получить два очка атрибута вместо одного при обдумывании сложных слов (20 уровень и выше)."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Вероятность получить два очка атрибута вместо одного растёт на 10%.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Вероятность получить два очка атрибута вместо одного растёт на 20%.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("За каждое десятое обдуманное слово вы гарантированно получаете два очка.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition PATH_TO_TRUTH = Cognition.builder("path_to_truth")
      .title("Путь к истине")
      .dependencies(INSIGHTFULNESS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволяет вам увереннее принимать решения с одинаковым исходом. Если вы четыре раза подряд принимаете решение, дающее вам очко одно и того же атрибута, то на четвёртый раз вы получаете два очка этого атрибута."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption(
               "Если вы три раза подряд принимаете решение, дающее вам очко одно и того же атрибута, то на третий раз вы получаете два очка этого атрибута."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption(
               "Если вы четыре раза подряд принимаете решение, дающее вам очко одно и того же атрибута, то на четвёртый раз вы получаете три очка этого атрибута."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption(
               "Если вы три раза подряд принимаете решение, дающее вам очко одно и того же атрибута, то на третий раз вы получаете три очка этого атрибута."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition LEAP_IN_DEVELOPMENT = Cognition.builder("leap_in_development")
      .title("Скачок в развитии")
      .dependencies(REFLECTION)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит вам лучше концентрироваться на обдумываемых словах, даже на тех, значения которых вы пока постичь не в состоянии. С шансом 10% у найденного слова может снизиться на 1 требуемый уровень для обдумывания."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(2, CognitionTierInfo.builder().desctiption("На 2.").level(-1).eidos(-1).type(AttributeType.BODY, -1).type(AttributeType.MIND, -1).build())
      .level(3, CognitionTierInfo.builder().desctiption("Шанс 20%.").level(-1).eidos(-1).type(AttributeType.BODY, -1).type(AttributeType.MIND, -1).build())
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("У всех слов снижается на 1 требуемый уровень для обдумывания.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition BANK = Cognition.builder("bank")
      .title("Банк")
      .dependencies(LEAP_IN_DEVELOPMENT)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки банка. Эта особая постройка помогает выйти из сложной финансовой ситуации. Предупреждение: инвестиции в эйдосы могут нести риски! Можно построить банк."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 3 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 5 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_BUILDER = Cognition.builder("specialization_builder")
      .title("Специализация: Строитель")
      .dependencies(WORKER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит рабочим формиксам стать строителями. Строители гораздо эффективнее чинят и сооружают здания. Открывает возможность смены специализации работяги на строителя."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition SAMURAI = Cognition.builder("samurai")
      .title("Самурай")
      .dependencies(SPECIALIZATION_SPEARMAN)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность нанимать самураев в казармах. Самураи – высококвалифицированные воины ближнего/среднего боя, способные наносить ощутимый урон. Открывает возможность производства самурая в казармах."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition CROSSBOWMAN = Cognition.builder("crossbowman")
      .title("Арбалетчик")
      .dependencies(ARCHER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность нанимать арбалетчиков в казармах. Арбалетчики – высококвалифицированные воины средне-дальней дистанции. Они способны наносить огромный урон. Открывает возможность производства  арбалетчика в казармах."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition SCIENTIST = Cognition.builder("scientist")
      .title("Учёный")
      .dependencies(LIBRARY)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность нанимать лучников в библиотеке. Учёные – универсальные формиксы, способные оптимизировать любые процессы. Открывает возможность делать в библиотеке учёных."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition MONK = Cognition.builder("monk")
      .title("Монах")
      .dependencies(TEMPLE)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность нанимать монахов в храме. Монахи – боевые формиксы, поддерживающие основные войска мантрами и молитвами. Можно изучить монаха. Спавнится в храме."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита, а также открывает пассивное особое умение.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_SPELLCASTER = Cognition.builder("specialization_spellcaster")
      .title("Специализация: Заклинатель")
      .dependencies(MONK)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность для монахов выучить боевую магию. Заклинатели – медленные воины, поражающие врагов могущественной магией стихий. Открывает возможность смены специализации монаха на боевого мага."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита, а также разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_OATHBREAKER = Cognition.builder("specialization_oathbreaker")
      .title("Специализация: Отступник")
      .dependencies(MONK)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность для монахов отринуть ложного бога. Клятвопреступники – опасные маги, владеющие большим арсеналом проклятий. Открывает возможность смены специализации на Клятвопреступника."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита, а также открывает пассивное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition MARKET = Cognition.builder("market")
      .title("Рынок")
      .dependencies(WILLPOWERFUL_DECISION, TEMPLE)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки рынка. Рынок – ключевой элемент товарооборота в мире. Всё во всех мирах имеет свою цену. Открывает возможность постройки рынка."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 3 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 6 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_MERCHANT = Cognition.builder("specialization_merchant")
      .title("Специализация: торговец")
      .dependencies(MARKET)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность нанимать торговцев на рынке. Каждый торговец занимается расширением ассортимента и регулированием цен рынка. Открывает создания юнита торговца."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует создание специальной постройки для торговцев.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition LABORATORY = Cognition.builder("laboratory")
      .title("Лаборатория")
      .dependencies(LIBRARY)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки лаборатории. Это особая постройка, позволяющая формиксам-учёным проводить сомнительные исследования. Открывает возможность постройки лаборатории."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 3 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 6 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня, а также разблокирует найм F0-рмика.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition GORGE_OF_DESPAIR = Cognition.builder("gorge_of_despair")
      .title("Ущелье отчаяния")
      .dependencies(EPILITH_CANYON)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Ущелья отчаяния – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition PALACE_GARDEN = Cognition.builder("palace_garden")
      .title("Дворцовый сад")
      .dependencies(GORGE_OF_DESPAIR)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Дворцового сада – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition BLACK_FORTRESS = Cognition.builder("black_fortress")
      .title("Чёрная крепость")
      .dependencies(PALACE_GARDEN)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Чёрной крепости – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition SILK_PLAINS = Cognition.builder("silk_plains")
      .title("Шёлковые равнины")
      .dependencies(BLACK_FORTRESS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Шёлковых равнин – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition HEAVENLY_ESTATE = Cognition.builder("heavenly_estate")
      .title("Небесное поместье")
      .dependencies(SILK_PLAINS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Небесного поместья – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.MIND, -1)
            .type(AttributeType.WILLPOWER, -1)
            .build()
      )
      .build();
   public static final Cognition GENEROUS_SOUL = Cognition.builder("generous_soul")
      .title("Щедрая душа")
      .dependencies(HEAD_ON_SHOULDERS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption("Познание этого навыка позволит вам лучше понимать концепцию подарков. Доставка подарков для друзей становится на 30% дешевле.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("При вручении подарка на 10% выше вероятность расположить другого игрока к себе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Вероятность получения подарков от других игроков увеличивается на 20%.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Доставка подарков для друзей становится бесплатной.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition SMALL_BUT_BOLD = Cognition.builder("small_but_bold")
      .title("Мал да удал")
      .dependencies(LEAP_IN_DEVELOPMENT)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption("Познание этого навыка позволит вам прочувствовать концепцию самостоятельности. Ресурсы, добываемые лично игроком, умножаются на 5.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Ресурсы, добываемые лично игроком, умножаются на 10.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Ресурсы, добытые игроком, приносят дополнительные эйдосы.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Враги, убитые игроком, могут натолкнуть на размышления.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition HEALER = Cognition.builder("healer")
      .title("Лекарь")
      .dependencies(LIBRARY)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность нанимать лекарей в библиотеке. Лекари – медицинские формиксы, способные исцелять повреждения других. Можно изучить лекаря. Спавнится в в библиотеке."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита, а также разблокирует активное и пассивное особые умения.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition SAFETY_TECHNIQUE = Cognition.builder("safety_technique")
      .title("Техника безопасности")
      .dependencies(SCIENTIST)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка позволит вам лучше отслеживать состояние ваших построек, делая их более прочными и лёгкими для ремонта. Постройки режут на 10% больше дамага от юнитов"
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Уменьшает порог при котором здание полностью рушится на 10%.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Уменьшает порог при котором здание полностью рушится еще на 10%.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Множитель времени ремонта увеличивается на 20% у зданий")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_CARPENTER = Cognition.builder("specialization_carpenter")
      .title("Специализация: Плотник")
      .dependencies(CARPENTRY_WORKSHOP)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность для рабочего переучиться на плотника. Плотники гораздо лучше работают в плотницкой мастерской, а также способны создавать особые предметы. Открывает возможность смены специализации работяги на плотника."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition SPECIALIZATION_BLACKSMITH = Cognition.builder("specialization_blacksmith")
      .title("Специализация: Кузнец")
      .dependencies(FORGE)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность для рабочего переучиться на кузнеца. Кузнецы специализируются на работе в кузнице, а также имеют неплохие боевые навыки. Открывает возможность смены специализации работяги на кузнеца."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует повышение уровня юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Открывает активное особое умение юнита.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition LIZARD_NEST = Cognition.builder("lizard_nest")
      .title("Гнездо ящеров")
      .dependencies(BARRACKS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка разблокирует в режиме контроля возможность постройки гнезда ящеров. В этой постройке возможен найм особых ездовых формиксов – ящеров с мощным хвостом и ядовитой слюной. Открывает возможность постройки гнездо ящеров. Открывает юнит Ящерица."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 3 уровня, а также открывает особое исследование.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 6 уровня, а также открывает активную особую способность.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует улучшение постройки до 10 уровня.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition FLAT_MOUNTAINS = Cognition.builder("flat_mountains")
      .title("Плоские горы")
      .dependencies(EDROV_GLACIER)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Плоских гор – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition FLOATING_ROCKS = Cognition.builder("floating_rocks")
      .title("Парящие скалы")
      .dependencies(FLAT_MOUNTAINS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Парящих скал – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition VALLEY_OF_WATERFALLS = Cognition.builder("valley_of_waterfalls")
      .title("Долина водопадов")
      .dependencies(FLOATING_ROCKS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Долины водопадов – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition TORN_FOREST = Cognition.builder("torn_forest")
      .title("Изорванный лес")
      .dependencies(VALLEY_OF_WATERFALLS)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Изорванного леса – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();
   public static final Cognition DEVILS_MAW = Cognition.builder("devils_maw")
      .title("Пасть дьявола")
      .dependencies(TORN_FOREST)
      .level(
         1,
         CognitionTierInfo.builder()
            .desctiption(
               "Познание этого навыка призовёт хранителя Пасти дьявола – *босснейм*. Как только хранитель будет повержен, будут доступны дальнейшие улучшения региона."
            )
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         2,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует месторождения в регионе.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         3,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особые точки интереса, а также открывает возможность регенерации региона.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .level(
         4,
         CognitionTierInfo.builder()
            .desctiption("Разблокирует особое событие региона, а также открывает возможность призыва хранителя.")
            .level(-1)
            .eidos(-1)
            .type(AttributeType.BODY, -1)
            .type(AttributeType.MIND, -1)
            .build()
      )
      .build();

   public static void init() {
   }
}

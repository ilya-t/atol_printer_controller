package com.atolprinterhelper;

import android.util.SparseArray;

public class PrintError {


    private int errorCode;
    private String errorDesc;
    public PrintError(int code){
        this.errorCode = code;
        errorDesc = errorList.get(code);

        if (errorDesc == null){
            errorDesc = "Неизвестный код ошибки ("+String.valueOf(this.errorCode)+")";
        }
    }

    public PrintError(DefaultPrintError error) {
        this.errorCode = error.code;
        this.errorDesc = error.description;
    }

    private static SparseArray<String> errorList;

    static {
        errorList = new SparseArray<>();
        errorList.append(0, "Ошибок нет");
        errorList.append(-1, "Нет связи");
        errorList.append(-3, "Порт недоступен");
        errorList.append(-4, "Ключ защиты не наийден");
        errorList.append(-5, "Работа драийвера прервана пользователем");
        errorList.append(-6, "Недопустимое значение");
        errorList.append(-7, "Невозможно добавить устроийство");
        errorList.append(-8, "Невозможно удалить устроийство");
        errorList.append(-9, "Логическое устроийство не наийдено");
        errorList.append(-10, "Неверная последовательность команд");
        errorList.append(-11, "Устроийство не включено");
        errorList.append(-12, "Не поддерживается в данноий версии оборудования");
        errorList.append(-13, "Драийвер не смог загрузить необходимые модули");
        errorList.append(-14, "Порт занят приложением (...)");
        errorList.append(-15, "Некорректные данные от устроийства");
        errorList.append(-16, "Не поддерживается в данном режиме устроийства");
        errorList.append(-17, "Нет больше элементов отчета");
        errorList.append(-19, "Нет доступа к ключу реестра");
        errorList.append(-199, "Описание ошибки недоступно");
        errorList.append(-3800,"В ККМ нет денег для выплаты");
        errorList.append(-3801,"Чек закрыт - операция невозможна");
        errorList.append(-3802,"Чек открыт - операция невозможна");
        errorList.append(-3803,"Неверная цена (сумма)");
        errorList.append(-3804,"Неверное количество");
        errorList.append(-3805,"Нулевая цена");
        errorList.append(-3807,"Нет бумаги");
        errorList.append(-3808,"Процесс ввода пароля");
        errorList.append(-3809,"Недопустимыий ИНН");
        errorList.append(-3810,"Накопление меньше суммы возврата или аннулирования");
        errorList.append(-3811,"Производится печать");
        errorList.append(-3813,"Неверная величина скидки/надбавки");
        errorList.append(-3814,"Операция после скидки/надбавки невозможна");
        errorList.append(-3815,"Неверная секция");
        errorList.append(-3816,"Неверныий вид оплаты");
        errorList.append(-3817,"Переполнение при умножении");
        errorList.append(-3818,"Операция запрещена в таблице настроек");
        errorList.append(-3819,"Переполнение итога чека");
        errorList.append(-3820,"Переполнение контрольноий ленты");
        errorList.append(-3821,"Открыт чек возврата – операция невозможна");
        errorList.append(-3822,"Смена превысила 24 часа");
        errorList.append(-3823,"Скидка запрещена в таблице");
        errorList.append(-3824,"Аннулирование и возврат в одном чеке");
        errorList.append(-3825,"Неверныий пароль");
        errorList.append(-3826,"Не переполнен буфер контрольноий ленты");
        errorList.append(-3827,"Идет печать контрольноий ленты");
        errorList.append(-3828,"Обнуленная касса (повторное гашение невозможно)");
        errorList.append(-3829,"Идет печать отчета");
        errorList.append(-3830,"Неверная дата");
        errorList.append(-3831,"Неверное время");
        errorList.append(-3832,"Сумма чека меньше суммы сторно");
        errorList.append(-3834,"Недопустимыий РНМ");
        errorList.append(-3835,"Вносимая сумма меньше суммы чека");
        errorList.append(-3836,"Начисление сдачи невозможно");
        errorList.append(-3837,"Смена открыта, операция невозможна");
        errorList.append(-3839,"Неверныий номер таблицы");
        errorList.append(-3840,"Неверныий номер ряда");
        errorList.append(-3841,"Неверныий номер поля");
        errorList.append(-3842,"ККМ заблокирована и ждет ввода пароля налогового инспектора");
        errorList.append(-3843,"Заводскоий номер уже задан");
        errorList.append(-3844,"Исчерпан лимит перерегистрациий");
        errorList.append(-3845,"Ошибка фискальноий памяти");
        errorList.append(-3846,"Запись фискальноий памяти повреждена");
        errorList.append(-3847,"Неверная смена");
        errorList.append(-3848,"Неверныий тип отчета");
        errorList.append(-3850,"Недопустимыий заводскоий номер ККМ");
        errorList.append(-3851,"ККМ не фискализирована");
        errorList.append(-3852,"Не задан заводскоий номер");
        errorList.append(-3853,"Нет отчетов");
        errorList.append(-3854,"Режим не активизирован");
        errorList.append(-3855,"Нет указанного чека в КЛ");
        errorList.append(-3857,"Некорректныий код или номер кода защиты");
        errorList.append(-3859,"Код защиты не введен");
        errorList.append(-3860,"Режим не поддерживается");
        errorList.append(-3862,"Повторная скидка/надбавка на операцию невозможна");
        errorList.append(-3863,"Скидка/надбавка на предыдущую операцию невозможна");
        errorList.append(-3864,"Открыт чек аннулирования – операция невозможна");
        errorList.append(-3865,"Открыт чек продажи – операция невозможна");
        errorList.append(-3866,"Требуется выполнение общего гашения");
        errorList.append(-3867,"Нет устроийства, обрабатывающего данную команду");
        errorList.append(-3868,"Нет связи с внешним устроийством");
        errorList.append(-3869,"Неверныий диапазон записеий");
        errorList.append(-3870,"Команда не разрешена введенными кодами защиты");
        errorList.append(-3871,"Невозможна отмена скидки/надбавки.");
        errorList.append(-3872,"Невозможно закрыть чек данным типом оплаты (в чеке присутствуют операции без контроля наличных)");
        errorList.append(-3873,"Нет связи с принтером чеков");
        errorList.append(-3877,"Неверныий номер маршрута");
        errorList.append(-3878,"Неверныий номер начальноий зоны");
        errorList.append(-3879,"Неверныий номер конечноий зоны");
        errorList.append(-3880,"Неверныий тип тарифа");
        errorList.append(-3881,"Неверныий тариф");
        errorList.append(-3882,"Нет заказа с таким номером");
        errorList.append(-3883,"Снятие отчета прервалось");
        errorList.append(-3887,"Неверныий штрихкод товара");
        errorList.append(-3892,"ККМ заблокирована после попытки ввода даты, меньшеий чем дата последнеий записи в ФП");
        errorList.append(-3893,"Требуется подтверждение ввода даты");
        errorList.append(-3894,"Отчет с гашением прерван. Вход в режим невозможен");
        errorList.append(-3895,"Неверная длина");
        errorList.append(-3896,"Сумма не наличных оплат превышает сумму чека");
        errorList.append(-3897,"Чек оплачен не полностью");
        errorList.append(-3898,"Неверныий номер картинки");
        errorList.append(-3899,"Невозможно открыть фаийл или формат некорректныий");
        errorList.append(-3900,"Сумма платежеий меньше суммы сторно");
        errorList.append(-3901,"Неверныий номер регистра");
        errorList.append(-3902,"Недопустимое целевое устроийство");
        errorList.append(-3903,"Неверныий параметр регистра");
        errorList.append(-3904,"Недостаточно памяти");
        errorList.append(-3905,"Неверныий формат или значение");
        errorList.append(-3906,"Переполнение суммы платежеий");
        errorList.append(-3907,"Ошибка обмена с фискальным модулем");
        errorList.append(-3908,"Переполнение ФП");
        errorList.append(-3909,"Ошибка печатающего устроийства");
        errorList.append(-3910,"Ошибка интерфеийса ЭКЛЗ");
        errorList.append(-3911,"Ошибка формата передачи параметров ЭКЛЗ");
        errorList.append(-3912,"Неверное состояние ЭКЛЗ");
        errorList.append(-3913,"Неисправимая ошибка ЭКЛЗ");
        errorList.append(-3914,"Авария крипто-процессора ЭКЛЗ");
        errorList.append(-3915,"Исчерпан временноий ресурс ЭКЛЗ");
        errorList.append(-3916,"ЭКЛЗ переполнено");
        errorList.append(-3917,"В ЭКЛЗ переданы неверная дата или время");
        errorList.append(-3918,"В ЭКЛЗ нет запрошенных данных");
        errorList.append(-3919,"Переполнение итога чека в ЭКЛЗ");
        errorList.append(-3920,"Исчерпан лимит активизациий");
        errorList.append(-3921,"Проверьте дату и время");
        errorList.append(-3922,"Дата и/или время в ККМ меньше чем в ЭКЛЗ");
        errorList.append(-3923,"Невозможно закрыть архив ЭКЛЗ");
        errorList.append(-3926,"Необходимо провести профилактические работы");
        errorList.append(-3927,"Переполнение сменного итога");
        errorList.append(-3928,"Активизация данноий ЭКЛЗ в составе данноий ККМ невозможна");
        errorList.append(-3929,"Переполнение счетчика наличности");
        errorList.append(-3930,"Переполнение буфера чека");
        errorList.append(-3931,"Размер картинки слишком большоий. Картинка не помещается на ленте.");
        errorList.append(-3932,"Неверныий тип чека");
        errorList.append(-3933,"Вход в режим заблокирован");
        errorList.append(-3934,"Неверные номера смен в ККМ и ЭКЛЗ");
        errorList.append(-3935,"ЭКЛЗ отсутствует");
        errorList.append(-3936,"Итоги чека ККМ и ЭКЛЗ не совпадают");
        errorList.append(-3937,"ККМ находится в режиме ввода даты или времени");
        errorList.append(-3938,"Переполнение буфера отложенного документа");
        errorList.append(-3939,"Невозможно напечатать второий фискальныий оттиск");
        errorList.append(-3940,"Буфер переполнен'");
        errorList.append(-3941,"Требуется гашение ЭЖ");
        errorList.append(-3942,"Перегрев головки принтера");
        errorList.append(-3943,"Ошибка отрезчика");
        errorList.append(-3944,"Буфер принтера ПД пустоий");
        errorList.append(-3945,"Буфер принтера ПД переполнен");
        errorList.append(-3946,"Карта физически отсутствует в картоприемнике");
        errorList.append(-3947,"Неверныий PIN код");
        errorList.append(-3948,"Ячеийка защищена от записи");
        errorList.append(-3949,"Невозможно сторно последнеий операции");
        errorList.append(-3950,"Сторно по коду невозможно");
        errorList.append(-3951,"Невозможен повтор последнеий операции");
        errorList.append(-3952,"Неверныий код товара");
        errorList.append(-3953,"Нет подтверждения или отмены продажи");
        errorList.append(-3954,"Отключение контроля наличности невозможно (не настроены необходимые типы оплаты)");
        errorList.append(-3955,"Товар не наийден");
        errorList.append(-3956,"Весовоий штрих-код с количеством <> 1.000");
        errorList.append(-3957,"Переполнение буфера чека");
        errorList.append(-3958,"Недостаточное количество товара");
        errorList.append(-3959,"Сторнируемое количество больше проданного");
        errorList.append(-3960,"Заблокированныий товар не наийден в буфере чека");
        errorList.append(-3961,"Данныий товар не продавался в чеке, сторно невозможно");
        errorList.append(-3962,"Memo Plus 3 заблокировано с ПК");
        errorList.append(-3963,"Ошибка контрольноий суммы таблицы настроек Memo Plus 3");
        errorList.append(-3964,"Идет обработка контрольноий ленты");
        errorList.append(-3965,"Недопустимыий пароль");
        errorList.append(-3966,"Ошибочное состояние ТРК");
        errorList.append(-3967,"Невозможно напечатать вторую фискальную копию");
        errorList.append(-3968,"Ошибка питания");
        errorList.append(-3969,"Сумма налога больше суммы регистрациий по чеку и/или итога");
        errorList.append(-3970,"Начисление налога на последнюю операцию невозможно");
    }

    public PrintError(String description) {
        errorCode = DefaultPrintError.FAIL.code;
        errorDesc = description;
    }

    public boolean isClear(){
        return errorCode == DefaultPrintError.SUCCESS.code;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
}

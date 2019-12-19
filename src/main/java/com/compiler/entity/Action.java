package com.compiler.entity;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class Action {

    @CsvBindByPosition(position = 0)
    private String timeStamp;

    @CsvBindByPosition(position = 1)
    private String userID;

    @CsvBindByPosition(position = 2)
    private String targetApp;

    @CsvBindByPosition(position = 3)
    private String eventType;

    @CsvBindByPosition(position = 4)
    private String url;

    @CsvBindByPosition(position = 5)
    private String content;

    @CsvBindByPosition(position = 6)
    private String workbookName;

    @CsvBindByPosition(position = 7)
    private String sheetName;

    @CsvBindByPosition(position = 8)
    private String targetId;

    @CsvBindByPosition(position = 9)
    private String targetClass;

    @CsvBindByPosition(position = 10)
    private String targetTagName;

    @CsvBindByPosition(position = 11)
    private String targetType;

    @CsvBindByPosition(position = 12)
    private String targetName;

    @CsvBindByPosition(position = 13)
    private String targetValue;

    @CsvBindByPosition(position = 14)
    private String targetInnerText;

    @CsvBindByPosition(position = 15)
    private String targetChecked;

    @CsvBindByPosition(position = 16)
    private String targetHref;

    @CsvBindByPosition(position = 17)
    private String targetOption;

    @CsvBindByPosition(position = 18)
    private String targetTitle;

    @CsvBindByPosition(position = 19)
    private String targetInnerHTML;

    private boolean inDeclarativeMode = false;

    public static Action createActionFromArray(String[] data) {
        Action action = new Action();

        action.timeStamp = data[0];
        action.userID = data[1];
        action.targetApp = data[2];
        action.eventType = data[3];
        action.url = data[4];
        action.content = data[5];
        action.workbookName = data[6];
        action.sheetName = data[7];
        action.targetId = data[8];
        action.targetClass = data[9];
        action.targetTagName = data[10];
        action.targetType = data[11];
        action.targetName = data[12];
        action.targetValue = data[13];
        action.targetInnerText = data[14];
        action.targetChecked = data[15];
        action.targetHref = data[16];
        action.targetOption = data[17];
        action.targetTitle = data[18];
        action.targetInnerHTML = data[19];

        return action;
    }

    public boolean isInDeclarativeMode() {
        return inDeclarativeMode;
    }

    public void setInDeclarativeMode(boolean inDeclarativeMode) {
        this.inDeclarativeMode = inDeclarativeMode;
    }
}

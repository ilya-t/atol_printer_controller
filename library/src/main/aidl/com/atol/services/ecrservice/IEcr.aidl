package com.atol.services.ecrservice;
import com.atol.services.ecrservice.ParcelableDate;

interface IEcr
{
	int		charLineLength();
	String	serialNumber();
	boolean	isFiscal();
	ParcelableDate dateTime();
	int		mode();
	int		subMode();
	int		checkState();
	int		checkNumber();
	int		docNumber();
	boolean	isSessionOpened();
	int		session();
	boolean	isPaperPresent();
	String	version();
	boolean	registerValueAsBoolean (in int num, in int arg);
	int		registerValueAsInt (in int num, in int arg);
	double	registerValueAsDouble (in int num, in int arg);
	ParcelableDate registerValueAsDate (in int num, in int arg);
	String	registerValueAsString (in int num, in int arg);
	
	boolean isDeviceEnabled();
	String	deviceSetting(in String name);
	void 	setDeviceSetting(in String name, in String value);
	String	deviceSettings();
	void	setDeviceSettings (in String settings);
	int		resultCode();
	String	resultDescription();
	String	badParamDescription();
	
	int		enableDevice(in boolean enable);
	int		beep();
	int		openDrawer();
	int		updateStatus();
	int		setDate(in ParcelableDate date);
	int		setTime(in ParcelableDate time);
	int		setMode(in int mode);
	int		resetMode();
	int		paperCut(in int type);
	int		printHeader();
	int		printFooter();
	int		printString(in String text, in int textWrap, in int alignment);						 
	int		printBarcode(in String barcode, in int alignment, in int height,
						 in int barcodeType, in double scale);	
	int		openCheck(in int type);
	int		payment(in double sum, in int type);
	int		cancelCheck();
	int		closeCheck(in int type);
	int		openSession(in String caption);
	int		report(in int type,	in int sessionNumber, in int docNumber, in boolean clearJournal);
	int		registration (in String title, in int textWrap, in int alignment, 
						in double quantity, in double price, in int department);	
	int		annulate (in String title, in int textWrap, in int alignment, 
						in double quantity, in double price, in boolean enableCheckSum);	
	int		refund (in String title, in int textWrap, in int alignment,
					in double quantity,	in double price, in boolean enableCheckSum);	
	int		storno (in String title, in int textWrap, in int alignment,
					in double quantity, in double price, in int department);	
	int		buy    (in String title, in int textWrap, in int alignment,
					in double quantity,	in double price, in int department,	in boolean enableCheckSum);
	int		annulateBuy (in String title, in int textWrap, in int alignment,
						in double quantity, in double price);	
	int		refundBuy (in String title, in int textWrap, in int alignment,
						in double quantity, in double price);
	int		cashIn (in double sum);
	int		cashOut (in double sum);
	
	int		getRegister (in int num, in int param1, in int param2);		
}
package com.hzti;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;
import org.junit.Assert;
import org.springframework.web.util.HtmlUtils;

import com.chinamilitary.util.HttpClientUtils;

public class WeizhangQuery {

	public static String URL = "http://www.hzti.com/service/qry/violation_veh.aspx?type=2&node=249";
	public static String AUTH_CODE_URL = "http://www.hzti.com/government/CreateCheckCode.aspx&d="+System.currentTimeMillis();
	static String VIEWSTATE = "/wEPDwUJNTI2ODIzNTY2D2QWAmYPZBYCAgMPZBYGAgcPZBYCZg8PFgIeBFRleHQFqx48dHI+PHRkIHdpZHRoPSc0MCcgY2xhc3M9J2xlZnRfdG9wXzAxJz4mbmJzcDs8L3RkPjx0ZCB3aWR0aD0nMjA5JyBjbGFzcz0nbGVmdF90b3BfMDEnPjx0YWJsZSB3aWR0aD0nMTAwJScgYm9yZGVyPScwJyBjZWxsc3BhY2luZz0nMCcgY2VsbHBhZGRpbmc9JzAnPjx0cj48dGQgaGVpZ2h0PScyMicgdmFsaWduPSdib3R0b20nIGNsYXNzPSdmb250dG9wMTInIG9ubW91c2VvdmVyPWdldGRlc2NyaXB0KCdkZXNjMjQ3JywndGl0bGUyNDcnKSA+PGEgaHJlZj1odHRwOi8vd3d3Lmh6dGkuY29tL3NlcnZpY2UvcXJ5L3ZlaF9pbmZvLmFzcHg/dHlwZT0yJm5vZGU9MjQ3IGNsYXNzPSdMaXN0VGFiJz7mnLrliqjovabln7rmnKzmn6Xor6I8L2E+PGlucHV0IGlkPSd0aXRsZTI0NycgdmFsdWU9J+acuuWKqOi9puWfuuacrOafpeivoicgdHlwZT0naGlkZGVuJyAvPjxpbnB1dCBpZD0nZGVzYzI0NycgdmFsdWU9J+afpeivouacuuWKqOi9pueahOWfuuacrOS/oeaBrycgdHlwZT0naGlkZGVuJyAvPjwvdGQ+PC90cj48L3RhYmxlPjwvdGQ+IDwvdHI+PHRyPjx0ZCB3aWR0aD0nNDAnIGNsYXNzPSdsZWZ0X3RvcF8wMSc+Jm5ic3A7PC90ZD48dGQgd2lkdGg9JzIwOScgY2xhc3M9J2xlZnRfdG9wXzAxJz48dGFibGUgd2lkdGg9JzEwMCUnIGJvcmRlcj0nMCcgY2VsbHNwYWNpbmc9JzAnIGNlbGxwYWRkaW5nPScwJz48dHI+PHRkIGhlaWdodD0nMjInIHZhbGlnbj0nYm90dG9tJyBjbGFzcz0nZm9udHRvcDEyJyBvbm1vdXNlb3Zlcj1nZXRkZXNjcmlwdCgnZGVzYzI0OCcsJ3RpdGxlMjQ4JykgPjxhIGhyZWY9aHR0cDovL3d3dy5oenRpLmNvbS9zZXJ2aWNlL3FyeS9wZWNjYW5jeS5hc3B4P3R5cGU9MiZub2RlPTI0OCBjbGFzcz0nTGlzdFRhYic+5py65Yqo6L2m546w5Zy66L+d5rOV5p+l6K+iPC9hPjxpbnB1dCBpZD0ndGl0bGUyNDgnIHZhbHVlPSfmnLrliqjovabnjrDlnLrov53ms5Xmn6Xor6InIHR5cGU9J2hpZGRlbicgLz48aW5wdXQgaWQ9J2Rlc2MyNDgnIHZhbHVlPSfmoLnmja7mnLrliqjovabmn6Xor6LnjrDlnLrlpITnvZrkv6Hmga8nIHR5cGU9J2hpZGRlbicgLz48L3RkPjwvdHI+PC90YWJsZT48L3RkPiA8L3RyPjx0cj48dGQgd2lkdGg9JzQwJyBjbGFzcz0nbGVmdF90b3BfMDEnPiZuYnNwOzwvdGQ+PHRkIHdpZHRoPScyMDknIGNsYXNzPSdsZWZ0X3RvcF8wMSc+PHRhYmxlIHdpZHRoPScxMDAlJyBib3JkZXI9JzAnIGNlbGxzcGFjaW5nPScwJyBjZWxscGFkZGluZz0nMCc+PHRyPjx0ZCBoZWlnaHQ9JzIyJyB2YWxpZ249J2JvdHRvbScgY2xhc3M9J2ZvbnR0b3AxMicgb25tb3VzZW92ZXI9Z2V0ZGVzY3JpcHQoJ2Rlc2MyNDknLCd0aXRsZTI0OScpID48YSBocmVmPWh0dHA6Ly93d3cuaHp0aS5jb20vc2VydmljZS9xcnkvdmlvbGF0aW9uX3ZlaC5hc3B4P3R5cGU9MiZub2RlPTI0OSBjbGFzcz0nTGlzdFRhYic+5py65Yqo6L2m6Z2e546w5Zy65p+l6K+iPC9hPjxpbnB1dCBpZD0ndGl0bGUyNDknIHZhbHVlPSfmnLrliqjovabpnZ7njrDlnLrmn6Xor6InIHR5cGU9J2hpZGRlbicgLz48aW5wdXQgaWQ9J2Rlc2MyNDknIHZhbHVlPSfmoLnmja7mnLrliqjovabmn6Xor6LooqvnlLXlrZDorablr5/jgIHmtYvpgJ/ku6rnrYnmn6XlpITnmoTov53ms5Xkv6Hmga8nIHR5cGU9J2hpZGRlbicgLz48L3RkPjwvdHI+PC90YWJsZT48L3RkPiA8L3RyPjx0cj48dGQgd2lkdGg9JzQwJyBjbGFzcz0nbGVmdF90b3BfMDEnPiZuYnNwOzwvdGQ+PHRkIHdpZHRoPScyMDknIGNsYXNzPSdsZWZ0X3RvcF8wMSc+PHRhYmxlIHdpZHRoPScxMDAlJyBib3JkZXI9JzAnIGNlbGxzcGFjaW5nPScwJyBjZWxscGFkZGluZz0nMCc+PHRyPjx0ZCBoZWlnaHQ9JzIyJyB2YWxpZ249J2JvdHRvbScgY2xhc3M9J2ZvbnR0b3AxMicgb25tb3VzZW92ZXI9Z2V0ZGVzY3JpcHQoJ2Rlc2MyNTAnLCd0aXRsZTI1MCcpID48YSBocmVmPWh0dHA6Ly93d3cuaHp0aS5jb20vc2VydmljZS9xcnkvc2ltcGxlLmFzcHg/dHlwZT0yJm5vZGU9MjUwIGNsYXNzPSdMaXN0VGFiJz7mnLrliqjovabnroDmmJPkuovmlYXmn6Xor6I8L2E+PGlucHV0IGlkPSd0aXRsZTI1MCcgdmFsdWU9J+acuuWKqOi9pueugOaYk+S6i+aVheafpeivoicgdHlwZT0naGlkZGVuJyAvPjxpbnB1dCBpZD0nZGVzYzI1MCcgdmFsdWU9J+agueaNruacuuWKqOi9puafpeivoueugOaYk+S6i+aVheafpeivoicgdHlwZT0naGlkZGVuJyAvPjwvdGQ+PC90cj48L3RhYmxlPjwvdGQ+IDwvdHI+PHRyPjx0ZCB3aWR0aD0nNDAnIGNsYXNzPSdsZWZ0X3RvcF8wMSc+Jm5ic3A7PC90ZD48dGQgd2lkdGg9JzIwOScgY2xhc3M9J2xlZnRfdG9wXzAxJz48dGFibGUgd2lkdGg9JzEwMCUnIGJvcmRlcj0nMCcgY2VsbHNwYWNpbmc9JzAnIGNlbGxwYWRkaW5nPScwJz48dHI+PHRkIGhlaWdodD0nMjInIHZhbGlnbj0nYm90dG9tJyBjbGFzcz0nZm9udHRvcDEyJyBvbm1vdXNlb3Zlcj1nZXRkZXNjcmlwdCgnZGVzYzI1MScsJ3RpdGxlMjUxJykgPjxhIGhyZWY9aHR0cDovL3d3dy5oenRpLmNvbS9zZXJ2aWNlL3FyeS9kcnZfaW5mby5hc3B4P3R5cGU9MiZub2RlPTI1MSBjbGFzcz0nTGlzdFRhYic+6am+6am25Lq65Z+65pys5p+l6K+iPC9hPjxpbnB1dCBpZD0ndGl0bGUyNTEnIHZhbHVlPSfpqb7pqbbkurrln7rmnKzmn6Xor6InIHR5cGU9J2hpZGRlbicgLz48aW5wdXQgaWQ9J2Rlc2MyNTEnIHZhbHVlPSfmn6Xor6Lpqb7pqbbkurrnmoTkv6Hmga/vvIzljIXlkKvorrDliIbkv6Hmga8nIHR5cGU9J2hpZGRlbicgLz48L3RkPjwvdHI+PC90YWJsZT48L3RkPiA8L3RyPjx0cj48dGQgd2lkdGg9JzQwJyBjbGFzcz0nbGVmdF90b3BfMDEnPiZuYnNwOzwvdGQ+PHRkIHdpZHRoPScyMDknIGNsYXNzPSdsZWZ0X3RvcF8wMSc+PHRhYmxlIHdpZHRoPScxMDAlJyBib3JkZXI9JzAnIGNlbGxzcGFjaW5nPScwJyBjZWxscGFkZGluZz0nMCc+PHRyPjx0ZCBoZWlnaHQ9JzIyJyB2YWxpZ249J2JvdHRvbScgY2xhc3M9J2ZvbnR0b3AxMicgb25tb3VzZW92ZXI9Z2V0ZGVzY3JpcHQoJ2Rlc2MyNTInLCd0aXRsZTI1MicpID48YSBocmVmPWh0dHA6Ly93d3cuaHp0aS5jb20vc2VydmljZS9xcnkvcGVjY2FuY3lfZHJ2LmFzcHg/dHlwZT0yJm5vZGU9MjUyIGNsYXNzPSdMaXN0VGFiJz7pqb7pqbbkurrnjrDlnLrov53ms5Xmn6Xor6I8L2E+PGlucHV0IGlkPSd0aXRsZTI1MicgdmFsdWU9J+mpvumptuS6uueOsOWcuui/neazleafpeivoicgdHlwZT0naGlkZGVuJyAvPjxpbnB1dCBpZD0nZGVzYzI1MicgdmFsdWU9J+agueaNrumpvumptuS6uuafpeivoueOsOWcuuWkhOe9mueahOS/oeaBrycgdHlwZT0naGlkZGVuJyAvPjwvdGQ+PC90cj48L3RhYmxlPjwvdGQ+IDwvdHI+PHRyPjx0ZCB3aWR0aD0nNDAnIGNsYXNzPSdsZWZ0X3RvcF8wMSc+Jm5ic3A7PC90ZD48dGQgd2lkdGg9JzIwOScgY2xhc3M9J2xlZnRfdG9wXzAxJz48dGFibGUgd2lkdGg9JzEwMCUnIGJvcmRlcj0nMCcgY2VsbHNwYWNpbmc9JzAnIGNlbGxwYWRkaW5nPScwJz48dHI+PHRkIGhlaWdodD0nMjInIHZhbGlnbj0nYm90dG9tJyBjbGFzcz0nZm9udHRvcDEyJyBvbm1vdXNlb3Zlcj1nZXRkZXNjcmlwdCgnZGVzYzI1MycsJ3RpdGxlMjUzJykgPjxhIGhyZWY9aHR0cDovL3d3dy5oenRpLmNvbS9zZXJ2aWNlL3FyeS9zaW1wbGVfZHJ2LmFzcHg/dHlwZT0yJm5vZGU9MjUzIGNsYXNzPSdMaXN0VGFiJz7pqb7pqbbkurrnroDmmJPkuovmlYXmn6Xor6I8L2E+PGlucHV0IGlkPSd0aXRsZTI1MycgdmFsdWU9J+mpvumptuS6uueugOaYk+S6i+aVheafpeivoicgdHlwZT0naGlkZGVuJyAvPjxpbnB1dCBpZD0nZGVzYzI1MycgdmFsdWU9J+agueaNrumpvumptuS6uuafpeivoueugOaYk+S6i+aVheS/oeaBrycgdHlwZT0naGlkZGVuJyAvPjwvdGQ+PC90cj48L3RhYmxlPjwvdGQ+IDwvdHI+ZGQCDQ8PFgIeClNvdXJjZURhdGEyyQ8AAQAAAP////8BAAAAAAAAAAwCAAAATlN5c3RlbS5EYXRhLCBWZXJzaW9uPTIuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OQUBAAAAFVN5c3RlbS5EYXRhLkRhdGFUYWJsZQMAAAAZRGF0YVRhYmxlLlJlbW90aW5nVmVyc2lvbglYbWxTY2hlbWELWG1sRGlmZkdyYW0DAQEOU3lzdGVtLlZlcnNpb24CAAAACQMAAAAGBAAAAJYHPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTE2Ij8+DQo8eHM6c2NoZW1hIHhtbG5zPSIiIHhtbG5zOnhzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYSIgeG1sbnM6bXNkYXRhPSJ1cm46c2NoZW1hcy1taWNyb3NvZnQtY29tOnhtbC1tc2RhdGEiPg0KICA8eHM6ZWxlbWVudCBuYW1lPSJUYWJsZSI+DQogICAgPHhzOmNvbXBsZXhUeXBlPg0KICAgICAgPHhzOnNlcXVlbmNlPg0KICAgICAgICA8eHM6ZWxlbWVudCBuYW1lPSJJRCIgdHlwZT0ieHM6ZGVjaW1hbCIgbXNkYXRhOnRhcmdldE5hbWVzcGFjZT0iIiBtaW5PY2N1cnM9IjAiIC8+DQogICAgICAgIDx4czplbGVtZW50IG5hbWU9IlBBUkVOVElEIiB0eXBlPSJ4czpkZWNpbWFsIiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iVElUTEUiIHR5cGU9InhzOnN0cmluZyIgbXNkYXRhOnRhcmdldE5hbWVzcGFjZT0iIiBtaW5PY2N1cnM9IjAiIC8+DQogICAgICAgIDx4czplbGVtZW50IG5hbWU9IlVSTCIgdHlwZT0ieHM6c3RyaW5nIiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgIDwveHM6c2VxdWVuY2U+DQogICAgPC94czpjb21wbGV4VHlwZT4NCiAgPC94czplbGVtZW50Pg0KICA8eHM6ZWxlbWVudCBuYW1lPSJOZXdEYXRhU2V0IiBtc2RhdGE6SXNEYXRhU2V0PSJ0cnVlIiBtc2RhdGE6TWFpbkRhdGFUYWJsZT0iVGFibGUiIG1zZGF0YTpVc2VDdXJyZW50TG9jYWxlPSJ0cnVlIj4NCiAgICA8eHM6Y29tcGxleFR5cGU+DQogICAgICA8eHM6Y2hvaWNlIG1pbk9jY3Vycz0iMCIgbWF4T2NjdXJzPSJ1bmJvdW5kZWQiIC8+DQogICAgPC94czpjb21wbGV4VHlwZT4NCiAgPC94czplbGVtZW50Pg0KPC94czpzY2hlbWE+BgUAAACGBjxkaWZmZ3I6ZGlmZmdyYW0geG1sbnM6bXNkYXRhPSJ1cm46c2NoZW1hcy1taWNyb3NvZnQtY29tOnhtbC1tc2RhdGEiIHhtbG5zOmRpZmZncj0idXJuOnNjaGVtYXMtbWljcm9zb2Z0LWNvbTp4bWwtZGlmZmdyYW0tdjEiPg0KICA8TmV3RGF0YVNldD4NCiAgICA8VGFibGUgZGlmZmdyOmlkPSJUYWJsZTEiIG1zZGF0YTpyb3dPcmRlcj0iMCI+DQogICAgICA8SUQ+MTwvSUQ+DQogICAgICA8UEFSRU5USUQ+MDwvUEFSRU5USUQ+DQogICAgICA8VElUTEU+5Lqk6YCa5L+h5oGv572RPC9USVRMRT4NCiAgICAgIDxVUkw+L2RlZmF1bHQuYXNw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FgIfAAX1CzxwPuS4uuS6huS/neaKpOaCqOeahOmakOengeS/oeaBr++8jOivt+WQjOaXtui+k+WFpeWPt+eJjOWPt+eggeWPijxzcGFuIHN0eWxlPSJjb2xvcjogI2ZmMDAwMCI+6L2m6L6G6K+G5Yir5Luj5Y+3PHN0cm9uZz48dT7mnIDlkI7vvJbkvY08L3U+PC9zdHJvbmc+77yI6KeB5LiL5Zu+77yJPC9zcGFuPu+8gTxiciAvPg0KJm5ic3A7Jm5ic3A7Jm5ic3A7IDxzdHJvbmc+6L2m6L6G6K+G5Yir5Luj5Y+377yaPC9zdHJvbmc+5Zyo6KGM6am26K+B5ZKM6L2m6L6G55m76K6w6K+B5Lmm5Lit5pyJ77yM5LiN6Laz5YWt5L2N55qE5oyJ5a6e6ZmF6ZW/5bqm6L6T5YWl77yM5YyF5ZCrJmxkcXVvOyomcmRxdW87562J56ym5Y+35Z2H5Li66L2m6L6G6K+G5Yir5Luj5Y+344CCPGJyIC8+DQombmJzcDsmbmJzcDsmbmJzcDsgPHN0cm9uZz7mn6Xor6LojIPlm7Q8L3N0cm9uZz7vvJrmna3lt57ovabovoblnKjmtZnmsZ/nnIHlhoXlkITlnLDluILkuqTorabjgIHpq5jpgJ/mna3lt57kuqTorabjgIHpk4Hot6/kuqTorabmlK/pmJ/jgIHmnLrlnLrkuqTorabjgIHmna3lt57luILooYzmlL/miafms5XlsYDnmoTov53ms5XmlbDmja7jgILmna3lt57luILvvIjljr/vvInljLrkuqTorabnmoTmlbDmja7nlLHkurrlt6XmoLjlr7nvvIznlLXlrZDorablr5/jgIHmtYvpgJ/ku6rmi43nhaflkI7nmoQzfjE15bel5L2c5pel5Y+v5Lul5Zyo572R5LiK5p+l6K+i44CC55yB5YaF6L2s6YCS5pWw5o2u5Zyo5LiA6Iez5Lik5ZGo5ZCO5Y+v5Zyo5pys572R56uZ5p+l6K+i44CCPGJyIC8+DQombmJzcDsmbmJzcDsmbmJzcDsg5pys572R56uZ5LiN5o+Q5L6b5aSW5Zyw6L2m6L6G55qE5py65Yqo6L2m6Z2e546w5Zy66L+d5rOV5pWw5o2u5p+l6K+i44CCPGJyIC8+DQo8c3Ryb25nPiZuYnNwOyZuYnNwOyZuYnNwOyDmn6Xor6Lnu5Pmnpw8L3N0cm9uZz7ooajnpLrmgqjov5jmnKrlpITnkIbnmoTpnZ7njrDlnLrov53ms5XooYzkuLrjgII8L3A+DQo8cD48aW1nIGFsdD0iIiB3aWR0aD0iNTAwIiBoZWlnaHQ9IjIzNyIgc3JjPSIvdXBsb2FkZmlsZXMvaW1hZ2UvJUU2JTlDJUJBJUU1JThBJUE4JUU4JUJEJUE2JUU5JTlEJTlFJUU3JThFJUIwJUU1JTlDJUJBJUU2JTlGJUE1JUU4JUFGJUEyJUU3JUE0JUJBJUU0JUJFJThCLmpwZyIgLz48L3A+DQo8cD7ovabovobor4bliKvku6Plj7fvvJo8YnIgLz4NCjxpbWcgYWx0PSIiIHdpZHRoPSI1MDAiIGhlaWdodD0iMzMzIiBzcmM9Ii91cGxvYWRmaWxlcy9pbWFnZS8lRTglQTElOEMlRTklQTklQjYlRTglQUYlODElRTglQkQlQTYlRTglQkUlODYlRTglQUYlODYlRTUlODglQUIlRTQlQkIlQTMlRTUlOEYlQjcuanBnIiAvPjxiciAvPg0KPGltZyBhbHQ9IiIgd2lkdGg9IjUwMCIgaGVpZ2h0PSI2MTQiIHNyYz0iL3VwbG9hZGZpbGVzL2ltYWdlLyVFOCVBMSU4QyVFOSVBOSVCNiVFOCVBRiU4MSVFOCVCRCVBNiVFOCVCRSU4NiVFOCVBRiU4NiVFNSU4OCVBQiVFNCVCQiVBMyVFNSU4RiVCNy0lRTclOTklQkIlRTglQUUlQjAlRTglQUYlODElRTQlQjklQTYuanBnIiAvPjwvcD5kZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WBAUQY3RsMDAkdG9wMSRHcmVlbgUPY3RsMDAkdG9wMSRCbHVlBQ9jdGwwMCR0b3AxJEdyYXkFDmN0bDAwJHRvcDEkUmVky6jaELBozes4IAiZk4bNLImBjsA=";
	static String EVENTVALIDATION = "/wEWIQLUyb6EBQLkw+O+CAL6w7/UBQLQ6IJpAtCOucUFApv01fQDArP61fQDAuuWiMUOApL9hMUOApf9ycgCAoL6rbcBAuDG1MoKArLyz0UCsPf44wkC1/3q3AkCvP+P9wwCw9qTlAQCv+mWpAsC092xxAQCr7DCVAKxxbVDAqbBzZ4BAsWfr90OAqbJoYIHAqux+5AFAtDu+ZoIAvPTxqcKAvKe3+kBAra/juIIAt/mqc4BAq7Nzy4CouSJzwgCgOLJY5iIPm7CoDs95N/DIuBBru+qa5oW";
	static String SESSION_ID = "mj0ye345abfd5t55ezuvpren";
	static byte[] CONTENT_BODY = null;
	static String COOKIE_PATH = System.getProperty("user.dir")+File.separator+"www.hzti.com"+File.separator+"cookie.txt";
	
	//初始化准备
	static{
		File file = null;
		try{
			file = new File(COOKIE_PATH);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.isDirectory() && !file.exists()){
				saveFile(COOKIE_PATH, "\r\n".getBytes(), false);
			}
			SESSION_ID = (String)IOUtils.readLines(new FileInputStream(COOKIE_PATH)).get(0);
			System.out.println("static:"+SESSION_ID);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 查询页面
	 *
	 */
	public static HashMap<String,String> queryHTML(byte[] content){
		Parser p1 = null;
		HashMap<String,String> values = new HashMap<String,String>();
		try{
			String body = new String(content,"UTF-8");
			p1 = new Parser();
			p1.setInputHTML(body);
			
			//获取__VIEWSTATE 参数
			NodeFilter fileter = new NodeClassFilter(InputTag.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("id", "__VIEWSTATE"));
			if(null != list && list.size() > 0){
				InputTag input = (InputTag)list.elementAt(0);
				String value = input.getAttribute("value");
				System.out.println("__VIEWSTATE" + ":" + HtmlUtils.htmlEscape(value));
			}
			
			//获取__EVENTTARGET 参数
			p1.setInputHTML(body);
			fileter = new NodeClassFilter(InputTag.class);
			list = p1.extractAllNodesThatMatch(fileter)
			.extractAllNodesThatMatch(
					new HasAttributeFilter("id", "__EVENTTARGET"));
			if(null != list && list.size() > 0){
				InputTag input = (InputTag)list.elementAt(0);
				Assert.assertNotNull(input);
				
				String value = input.getAttribute("value");
				Assert.assertNotNull(value);
				values.put("__EVENTTARGET", value);
			}
			
			//获取__EVENTARGUMENT 参数
			p1.setInputHTML(body);
			fileter = new NodeClassFilter(InputTag.class);
			list = p1.extractAllNodesThatMatch(fileter)
			.extractAllNodesThatMatch(
					new HasAttributeFilter("id", "__EVENTARGUMENT"));
			if(null != list && list.size() > 0){
				InputTag input = (InputTag)list.elementAt(0);
				Assert.assertNotNull(input);
				
				String value = input.getAttribute("value");
				Assert.assertNotNull(value);
				values.put("__EVENTARGUMENT", value);
			}
			
			//获取__EVENTVALIDATION 参数
			p1.setInputHTML(body);
			fileter = new NodeClassFilter(InputTag.class);
			list = p1.extractAllNodesThatMatch(fileter)
			.extractAllNodesThatMatch(
					new HasAttributeFilter("id", "__EVENTVALIDATION"));
			if(null != list && list.size() > 0){
				InputTag input = (InputTag)list.elementAt(0);
				Assert.assertNotNull(input);
				
				String value = input.getAttribute("value");
				Assert.assertNotNull(value);
				values.put("__EVENTVALIDATION", value);
			}
			
			
			//获取__VIEWSTATE 参数
			p1.setInputHTML(body);
			fileter = new NodeClassFilter(InputTag.class);
			list = p1.extractAllNodesThatMatch(fileter)
			.extractAllNodesThatMatch(
					new HasAttributeFilter("id", "__VIEWSTATE"));
			if(null != list && list.size() > 0){
				InputTag input = (InputTag)list.elementAt(0);
				Assert.assertNotNull(input);
				
				String value = input.getAttribute("value");
				Assert.assertNotNull(value);
				values.put("__VIEWSTATE", value);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1){
				p1 = null;
			}
		}
		return values;
	}
	
	public static void doQuery(HashMap<String,String> params){
		URL url = null;
		HttpURLConnection connection = null;
		OutputStream out = null;
		InputStream in = null;
		ByteArrayOutputStream byteArray = null;
		StringBuffer sb = new StringBuffer();
		try{
			url = new URL(URL);
			connection = (HttpURLConnection)url.openConnection();
			connection.addRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			connection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			connection.addRequestProperty("Connection", "close"); //keep-alive
			connection.addRequestProperty("Origin", "http://www.hzti.com");
			connection.addRequestProperty("X-MicrosoftAjax", "Delta=true");
			connection.addRequestProperty("Accept", "*/*");
			connection.addRequestProperty("Referer", "http://www.hzti.com/service/qry/violation_veh.aspx?type=2&node=249"); //?type=2&node=249
			connection.addRequestProperty("Cookie", "ASP.NET_SessionId="+SESSION_ID+"; isLoginedWeb=T");
			connection.addRequestProperty("Cache-Control", "no-cache");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setReadTimeout(10*1000);
			connection.setConnectTimeout(15*1000);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();
			
			sb.append(URLEncoder.encode("ctl00$ScriptManager1", "UTF-8")+"="+URLEncoder.encode("ctl00$ContentPlaceHolder1$UpdatePanel1|ctl00$ContentPlaceHolder1$Button1", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$hpzl", "UTF-8")+"="+URLEncoder.encode("小型汽车", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$steelno", "UTF-8")+"="+URLEncoder.encode("浙A249NT", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$identificationcode", "UTF-8")+"="+URLEncoder.encode("005953", "UTF-8"));
			sb.append("&");
			sb.append(URLEncoder.encode("ctl00$ContentPlaceHolder1$checkcode", "UTF-8")+"="+URLEncoder.encode("ttiq", "UTF-8"));
			sb.append("&");
			sb.append("__EVENTTARGET=");
			sb.append("&");
			sb.append("__EVENTARGUMENT=");
			sb.append("&");
			sb.append("__VIEWSTATE="+URLEncoder.encode(params.get("__VIEWSTATE"),"UTF-8"));
			sb.append("&");
			sb.append("__EVENTVALIDATION="+URLEncoder.encode(params.get("__EVENTVALIDATION"),"UTF-8"));
			
			sb.append("&");
			sb.append("__ASYNCPOST=true");
			sb.append("&");
			sb.append("ctl00$ContentPlaceHolder1$Button1="+URLEncoder.encode("查询", "UTF-8"));
			String requestBody = sb.toString();
			System.out.println("提交内容:\r\n"+requestBody);
			out = connection.getOutputStream();
			out.write(requestBody.getBytes());
			out.flush();
			out.close();
			
			int code = connection.getResponseCode();
			switch(code){
				case 200:
					in = connection.getInputStream();
					byteArray = new ByteArrayOutputStream();
					int ch;
					while((ch = in.read()) != -1){
						byteArray.write(ch);
					}
					byteArray.flush();
					String result = byteArray.toString("UTF-8");
					if(result.startsWith("15|error|500")){
						System.out.println("出现错误，需要解决\r\n"+result);
						System.out.println("准备再次去服务器获取SID数据");
						//TODO 重新获取Cookie中的ASP.NET_SessionId
						initHeader();
					}else{
						System.out.println("响应内容:"+"\r\n"+result);
					}
					break;
				default:
					System.err.println(connection.getResponseCode()+":"+connection.getResponseMessage());
					break;
			}
			
			if(null != out){
				out.close();
			}
			if(null != in){
				in.close();
			}
		}catch(Exception e){
			if(null != byteArray){
				try {
					byteArray.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			if(null != connection){
				connection.disconnect();
				connection = null;
			}
			
			if(null != url){
				url = null;
			}
			sb = null;
			e.printStackTrace();
		}
	}
	
	static void query(){
		
		byte[] content = null;
		long start = System.currentTimeMillis();
		content = HttpClientUtils.getResponseBodyAsByte("http://www.hzti.com/service/qry/violation_veh.aspx?type=2&node=249", null, URL);
		if(null == content){
			System.out.println(" > 获取网页数据为空");
			return;
		}
		
		//获取一把验证码
		HttpClientUtils.validationURL(AUTH_CODE_URL);
		
		long spend = (System.currentTimeMillis()-start)/1000;
		System.out.println(">> 获取HTML耗时："+(System.currentTimeMillis()-start)+" ms");
		System.out.println(">> 下载速度："+((content.length/spend)/1024)+"."+((content.length/spend)%1024)+" KB/s");
		
		HashMap<String,String> map = queryHTML(content);
		doQuery(map);
		
//		StringBuffer sb = new StringBuffer();
//		Iterator it = map.keySet().iterator();
//		while(it.hasNext()){
//			String key = (String)it.next();
//			String value = map.get(key);
//			sb.append(key).append(":").append(value);
//		}
		
/*		System.out.println(VIEWSTATE);
		System.out.println(HtmlUtils.htmlEscape(VIEWSTATE));
		try{
			System.out.println(URLEncoder.encode(VIEWSTATE,"UTF-8"));
		}catch(Exception e){
		}
*/	}
	
	/**
	 * 获取车辆类型
	 * @param url
	 */
	static void getCarType(String url){
		Parser p1 = null;
		try{
			byte[] content = null;
			content = HttpClientUtils.getResponseBodyAsByte("http://www.hzti.com/service/qry/violation_veh.aspx?type=2&node=249", null, URL);
			String body = new String(content,"UTF-8");
			p1 = new Parser();
			p1.setInputHTML(body);
			
			NodeFilter fileter = new NodeClassFilter(SelectTag.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("id", "ctl00_ContentPlaceHolder1_hpzl"));
			if(null != list && list.size() > 0){
				StringBuffer sb = new StringBuffer();
				SelectTag select = (SelectTag)list.elementAt(0);
				OptionTag[] ops = select.getOptionTags();
				for(OptionTag op:ops){
					sb.append("<item>").append(op.getValue()).append("</item>").append("\r\n");
				}
				System.out.println(sb.toString());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1)
				p1 = null;
		}
		
	}
	
	/**
	 * 初始化数据获取网站正文
	 * 
	 */
	private static void init(){
		URL url = null;
		HttpURLConnection connection = null;
		InputStream is = null;
		ByteArrayOutputStream out = null;
		byte[] result = null;
		String sid = null;
		try{
			url = new URL(URL);
			connection = (HttpURLConnection)url.openConnection();
			connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			connection.addRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			connection.addRequestProperty("Connection", "close");//keep-alive
			connection.addRequestProperty("Cache-Control", "max-age=0");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
			connection.setReadTimeout(10*1000);
			connection.setConnectTimeout(15*1000);
			connection.connect();
			
			int code = connection.getResponseCode();
			if (code == HttpURLConnection.HTTP_OK) {
				int length = connection.getContentLength();
				if (length > 0) {
					System.out.println("网页内容大小:"+length);
					out = new ByteArrayOutputStream();
					is = connection.getInputStream();
					int ch;
					while ((ch = is.read()) != -1) {
						out.write(ch);
					}
					out.flush();
					result = out.toByteArray();
				}
			}
			CONTENT_BODY = result;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != connection)
				connection.disconnect();
		}
	}
	
	private static void initHeader(){
		URL url = null;
		HttpURLConnection connection = null;
		String sid = null;
		try{
			url = new URL(URL);
			connection = (HttpURLConnection)url.openConnection();
			connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			connection.addRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			connection.addRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			connection.addRequestProperty("Connection", "keep-alive");
			connection.addRequestProperty("Cache-Control", "max-age=0");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
			connection.setReadTimeout(10*1000);
			connection.setConnectTimeout(15*1000);
			connection.connect();
			
			Map<String, List<String>> responseHeader = connection.getHeaderFields();
			Set<String> keySet = responseHeader.keySet();
			Iterator iterator = keySet.iterator();
			boolean isOk = true;
			while(iterator.hasNext() && isOk){
				String key = (String)iterator.next();
				if(null != key){
					if(key.equals("Set-Cookie")){
						List<String> valueList = responseHeader.get(key);
						for(String value:valueList){
							if(value.contains("ASP.NET_SessionId")){
								sid= value.substring(value.indexOf("=")+1,value.indexOf(";"));
								System.out.println("老版本:SESSION_ID:"+SESSION_ID);
								//TODO 写入临时文件
								String tmpId = (String)IOUtils.readLines(new FileInputStream(COOKIE_PATH)).get(0);
								if(!tmpId.equals(sid)){
									SESSION_ID = sid;
									saveFile(COOKIE_PATH,SESSION_ID.getBytes(),false);
								}
								isOk = false;
								break;
							}
						}
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != connection)
				connection.disconnect();
		}
	}
	
	private static void getHeader2(){
		
		String tmp = HttpClientUtils.getHttpHeaderResponse(URL,"Set-Cookie");
		System.out.println(tmp+"\r\n");
		HashMap<String, String>  map = HttpClientUtils.getHttpHeaderResponse(URL);
		StringBuffer sb = new StringBuffer();
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			if(key.equals("Set-Cookie")){
				String value = map.get(key);
				sb.append(key).append(":").append(value).append("\r\n");
				System.out.println(sb.toString());
			}
		}
	}
	
    /**
     * 将字节数组保存在指定文件中
     * 
     * @param pathname
     *            需要保存的文件路径
     * @param buffer
     *            需要被保存的内容
     * @param append
     *            是否采用追加的方式
     * @return
     */
    public static int saveFile(String pathname, byte[] buffer, boolean append) {
        String path = null;
        pathname = pathname.replaceAll("\\\\", "/");
        path = pathname.substring(0, pathname.lastIndexOf("/"));
        java.io.File dir = new java.io.File(path);
        if (!dir.getParentFile().exists())
            dir.getParentFile().mkdirs();// 创建不存在的目录
        try {
            OutputStream bos = new FileOutputStream(pathname, append);
            bos.write(buffer);
            bos.close();
        } catch (FileNotFoundException fnfe) {
            return -1;
        } catch (IOException ioe) {
            return -2;
        }
        return 0;
    }
    
    
    
	public static void main(String args[]){
//		init();
		query();
//		System.out.println("\r\n");
//		getHeader2();
//		String tmpId = null;
//		try {
//			tmpId = (String)IOUtils.readLines(new FileInputStream(COOKIE_PATH)).get(0);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("|"+tmpId+"|");
	}
}

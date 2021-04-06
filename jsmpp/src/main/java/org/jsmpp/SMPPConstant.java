/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsmpp;

/**
 * This contains all of the SMPP constants.
 * <ul>
 *  <li><b>"CID"</b> prefix is for <b>command_id</b></li>
 *  <li><b>"STAT"</b> prefix is for <b>command_status</b></li>
 * </ul>
 *
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public interface SMPPConstant {
    /*
     * Utility SMPP constants
     */

    int PDU_HEADER_LENGTH = 16;

    /**
     * Is the MASK for the response command_id.
     */
    int MASK_CID_RESP = 0x80000000;

    /*
     * Command ID constants (CID prefix).
     */
    int CID_BIND_RECEIVER = 0x00000001;
    int CID_BIND_TRANSMITTER = 0x00000002;
    int CID_QUERY_SM = 0x00000003;
    int CID_SUBMIT_SM = 0x00000004;
    int CID_DELIVER_SM = 0x00000005;
    int CID_UNBIND = 0x00000006;
    int CID_REPLACE_SM = 0x00000007;
    int CID_CANCEL_SM = 0x00000008;
    int CID_BIND_TRANSCEIVER = 0x00000009;
    int CID_OUTBIND = 0x0000000B;
    int CID_ENQUIRE_LINK = 0x00000015;
    int CID_SUBMIT_MULTI = 0x00000021;
    int CID_ALERT_NOTIFICATION = 0x00000102;
    int CID_DATA_SM = 0x00000103;
    int CID_BROADCAST_SM = 0x00000111;
    int CID_QUERY_BROADCAST_SM = 0x00000112;
    int CID_CANCEL_BROADCAST_SM = 0x00000113;
    int CID_GENERIC_NACK = 0x80000000;
    int CID_BIND_RECEIVER_RESP = 0x80000001;
    int CID_BIND_TRANSMITTER_RESP = 0x80000002;
    int CID_QUERY_SM_RESP = 0x80000003;
    int CID_SUBMIT_SM_RESP = 0x80000004;
    int CID_DELIVER_SM_RESP = 0x80000005;
    int CID_UNBIND_RESP = 0x80000006;
    int CID_REPLACE_SM_RESP = 0x80000007;
    int CID_CANCEL_SM_RESP = 0x80000008;
    int CID_BIND_TRANSCEIVER_RESP = 0x80000009;
    int CID_ENQUIRE_LINK_RESP = 0x80000015;
    int CID_SUBMIT_MULTI_RESP = 0x80000021;
    int CID_DATA_SM_RESP = 0x80000103;
    int CID_BROADCAST_SM_RESP = 0x80000111;
    int CID_QUERY_BROADCAST_SM_RESP = 0x80000112;
    int CID_CANCEL_BROADCAST_SM_RESP = 0x80000113;

    /*
     * Command Status constants (STAT prefix).
     */
    int STAT_ESME_ROK = 0x00000000;
    int STAT_ESME_RINVMSGLEN = 0x00000001;
    int STAT_ESME_RINVCMDLEN = 0x00000002;
    int STAT_ESME_RINVCMDID = 0x00000003;

    /**
     * Incorrect BIND Status for given command.
     *
     * PDU has been sent in the wrong session state.
     * E.g. sending a submit_sm without first establishing a Bound_TX session state.
     */
    int STAT_ESME_RINVBNDSTS = 0x00000004;

    /**
     * ESME already in Bound State.
     */
    int STAT_ESME_RALYBND = 0x00000005;
    int STAT_ESME_RINVPRTFLG = 0x00000006;
    int STAT_ESME_RINVREGDLVFLG = 0x00000007;
    int STAT_ESME_RSYSERR = 0x00000008;
    int STAT_ESME_RINVSRCADR = 0x0000000a;
    int STAT_ESME_RINVDSTADR = 0x0000000b;
    int STAT_ESME_RINVMSGID = 0x0000000c;
    /**
     * Bind Failed.
     */
    int STAT_ESME_RBINDFAIL = 0x0000000d;
    int STAT_ESME_RINVPASWD = 0x0000000e;
    int STAT_ESME_RINVSYSID = 0x0000000f;
    int STAT_ESME_RCANCELFAIL = 0x00000011;
    int STAT_ESME_RREPLACEFAIL = 0x00000013;
    /**
     * Message queue full.
     */
    int STAT_ESME_RMSGQFUL = 0x00000014;
    int STAT_ESME_RINVSERTYP = 0x00000015;
    int STAT_ESME_RINVNUMDESTS = 0x00000033;
    int STAT_ESME_RINVDLNAME = 0x00000034;
    int STAT_ESME_RINVDESTFLAG = 0x00000040;
    int STAT_ESME_RINVSUBREP = 0x00000042;
    int STAT_ESME_RINVESMCLASS = 0x00000043;
    int STAT_ESME_RCNTSUBDL = 0x00000044;
    int STAT_ESME_RSUBMITFAIL = 0x00000045;
    int STAT_ESME_RINVSRCTON = 0x00000048;
    int STAT_ESME_RINVSRCNPI = 0x00000049;
    int STAT_ESME_RINVDSTTON = 0x00000050;
    int STAT_ESME_RINVDSTNPI = 0x00000051;
    int STAT_ESME_RINVSYSTYP = 0x00000053;
    int STAT_ESME_RINVREPFLAG = 0x00000054;
    int STAT_ESME_RINVNUMMSGS = 0x00000055;
    int STAT_ESME_RTHROTTLED = 0x00000058;
    int STAT_ESME_RINVSCHED = 0x00000061;
    int STAT_ESME_RINVEXPIRY = 0x00000062;
    /**
     * Predefined Message Invalid or Not Found.
     */
    int STAT_ESME_RINVDFTMSGID = 0x00000063;
    /**
     * ESME Receiver Temporary App Error Code
     */
    int STAT_ESME_RX_T_APPN = 0x00000064;
    /**
     * ESME Receiver Permanent App Error Code
     */
    int STAT_ESME_RX_P_APPN = 0x00000065;
    /**
     * ESME Receiver Reject App Error Code
     */
    int STAT_ESME_RX_R_APPN = 0x00000066;
    int STAT_ESME_RQUERYFAIL = 0x00000067;
    int STAT_ESME_RINVTLVSTREAM = 0x000000c0;
    int STAT_ESME_RTLVNOTALLWD = 0x000000c1;
    int STAT_ESME_RINVTLVLEN = 0x000000c2;
    int STAT_ESME_RMISSINGTLV = 0x000000c3;
    int STAT_ESME_RINVTLVVAL = 0x000000c4;
    int STAT_ESME_RDELIVERYFAILURE = 0x000000fe;
    int STAT_ESME_RUNKNOWNERR = 0x000000ff;
    int STAT_ESME_RSERTYPUNAUTH = 0x00000100;
    int STAT_ESME_RPROHIBITED = 0x00000101;
    int STAT_ESME_RSERTYPUNAVAIL = 0x00000102;
    int STAT_ESME_RSERTYPDENIED = 0x00000103;
    int STAT_ESME_RINVDCS = 0x00000104;
    int STAT_ESME_RINVSRCADDRSUBUNIT = 0x00000105;
    int STAT_ESME_RINVDSTADDRSUBUNIT = 0x00000106;
    int STAT_ESME_RINVBCASTFREQINT = 0x00000107;
    int STAT_ESME_RINVBCASTALIAS_NAME = 0x00000108;
    int STAT_ESME_RINVBCASTAREAFMT = 0x00000109;
    int STAT_ESME_RINVNUMBCAST_AREAS = 0x0000010a;
    int STAT_ESME_RINVBCASTCNTTYPE = 0x0000010b;
    int STAT_ESME_RINVBCASTMSGCLASS = 0x0000010c;
    int STAT_ESME_RBCASTFAIL = 0x0000010d;
    int STAT_ESME_RBCASTQUERYFAIL = 0x0000010e;
    int STAT_ESME_RBCASTCANCELFAIL = 0x0000010f;
    int STAT_ESME_RINVBCAST_REP = 0x00000110;
    int STAT_ESME_RINVBCASTSRVGRP = 0x00000111;
    int STAT_ESME_RINVBCASTCHANIND = 0x00000112;

    /*
     * ESM class parameter constant
     */
    // ESME -> SMSC Messaging Mode.
    byte ESMCLS_DEFAULT_MODE = 0x00;
    byte ESMCLS_DATAGRAM_MODE = 0x01;
    byte ESMCLS_FORWARD_MODE = 0x02;
    byte ESMCLS_STORE_FORWARD = 0x03;

    /**
     * Encoded esm_class parameter. xx0000xx
     */
    byte ESMCLS_DEFAULT_MESSAGE_TYPE = 0x00;

    // ESME -> SMSC Message Type.
    /**
     * Short Message contains ESME Delivery Acknowledgment. xx0010xx
     */
    byte ESMCLS_ESME_DELIVERY_ACK = 0x08;
    /**
     * Short Message contains ESME Manual/User Acknowledgment. xx0100xx
     */
    byte ESMCLS_ESME_MANUAL_ACK = 0x10;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -&gt; ESME)
     * xx0001xx Short Message contains SMSC Delivery Receipt.
     */
    byte ESMCLS_SMSC_DELIV_RECEIPT = 0x04;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -&gt; ESME)
     * xx0001xx Short Message contains SME Delivery Acknowledgment.
     */
    byte ESMCLS_SME_DELIV_ACK = 0x08;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -&gt; ESME)
     * xx0100xx Short Message contains SME Manual/User Acknowledgment.
     */
    byte ESMCLS_SME_MANUAL_ACK = 0x10;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -&gt; ESME)
     * xx0110xx Short Message contains Conversation Abort (Korean CDMA).
     */
    byte ESMCLS_CONV_ABORT = 0x18;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -&gt; ESME)
     * xx1000xx Short Message contains Intermediate Delivery Notification.
     */
    byte ESMCLS_INTRMD_DELIV_NOTIF = 0x20;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -&gt; ESME)
     * 01xxxxxx UDHI Indicator set.
     */
    byte ESMCLS_UDHI_INDICATOR_SET = 0x40;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -&gt; ESME)
     * 10xxxxxx Reply Path.
     */
    byte ESMCLS_REPLY_PATH = (byte)0x80;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -&gt; ESME)
     * 11xxxxxx UDHI and Reply Path can use.
     */
    byte ESMCLS_UDHI_REPLY_PATH = (byte)0xc0;

    /*
     * SMSC Delivery Receipt of registered delivery. bits 1 and 0
     */

    /**
     * No SMSC Delivery receipt. xxxxxx00
     */
    byte REGDEL_SMSC_NO = 0x00;

    /**
     * SMSC Delivery Receipt requested where final delivery outcome is delivery
     * success or failure. xxxxxx01
     */
    byte REGDEL_SMSC_SUCCESS_FAILURE_REQUESTED = 0x01;

    /**
     * SMSC Deliver Receipt requested where final delivery outcome is delivery
     * failure. xxxxxx10
     */
    byte REGDEL_SMSC_FAILURE_REQUESTED = 0x02;

    /*
     * SME originated Acknowledgment of registered delivery. bits 3 and 2
     */

    /**
     * No recipient SME Acknowledgment requested. xxxx00xx
     */
    byte REGDEL_SME_ACK_NO = 0x00;

    /**
     * SME Delivery Acknowledgment requested. xxxx01xx
     */
    byte REGDEL_SME_DELIVERY_ACK_REQUESTED = 0x04;

    /**
     * SME Manual/User Acknowledgment requested. xxxx10xx
     */
    byte REGDEL_SME_MANUAL_ACK_REQUESTED = 0x08;

    /**
     * Both Delivery and Manual/User Acknowledgment requested.
     */
    byte REGDEL_SME_DELIVERY_MANUAL_ACK_REQUESTED = 0x0c;

    /*
     * Data Coding (See also Alphabet class)
     */

    /**
     * SMSC Default Alphabet. 00000000
     */
    byte DC_DEFAULT = 0x00;

    /**
     * IA5 (CCITT T.50) / ASCII (ANSI X3.4)
     */
    byte DC_IA5 = 0x01;

    /**
     * Latin 1 (ISO-8859-1)
     */
    byte DC_LATIN1 = 0x03;

    /**
     * Octet unspecified (8-bit binary)
     */
    byte DC_BINARY = 0x04;

    /**
     * JIS (X 0208-1990)
     */
    byte DC_JIS = 0x05;

    /**
     * Cyrillic (ISO-8859-5)
     */
    byte DC_CYRILLIC = 0x06;

    /**
     * Latin/Hebrew (ISO-8859-8)
     */
    byte DC_LATIN_HEBREW = 0x07;

    /**
     * UCS2 (ISO/IEC-10646) 00001000
     */
    byte DC_UCS2 = 0x08;

    /*
     * interface_version
     */
    byte IF_VERSION_00 = 0x00;
    byte IF_VERSION_33 = 0x33;
    byte IF_VERSION_34 = 0x34;
    byte IF_VERSION_50 = 0x50;

    /*
     * Type of Number
     */
    byte TON_UNKNOWN = 0x00;
    byte TON_INTERNATIONAL = 0x01;
    byte TON_NATIONAL = 0x02;
    byte TON_NETWORK_SPECIFIC = 0x03;
    byte TON_SUBSCRIBER_NUMBER = 0x04;
    byte TON_ALPHANUMERIC = 0x05;
    byte TON_ABBREVIATED = 0x06;

    /*
     * Numeric Plan Indicator
     */
    byte NPI_UNKNOWN = 0x00;
    byte NPI_ISDN = 0x01;
    byte NPI_DATA = 0x03;
    byte NPI_TELEX = 0x04;
    byte NPI_LAND_MOBILE = 0x06;
    byte NPI_NATIONAL = 0x08;
    byte NPI_PRIVATE = 0x09;
    byte NPI_ERMES = 0x0a;
    byte NPI_INTERNET = 0x0e;
    byte NPI_WAP = 0x12;

    short TAG_SC_INTERFACE_VERSION = 0x0210;
    short TAG_SAR_MSG_REF_NUM = 0X020c;
    short TAG_SAR_TOTAL_SEGMENTS = 0x020e;
    short TAG_SAR_SEGMENT_SEQNUM = 0x020f;

}

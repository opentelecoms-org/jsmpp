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
 * This contains all of the smpp constants.
 * <ul>
 *  <li><b>"CID"</b> prefix is for <b>command_id</b></li>
 *  <li><b>"STAT"</b> prefix is for <b>command_status</b></li>
 * </ul>
 * 
 * @author uudashr
 * @version 1.0
 * @version 1.0
 * 
 */
public interface SMPPConstant {
    /*
     * Untility constant
     */
    /**
     * Is the MASK for the response command_id.
     */
    public static final int MASK_CID_RESP = 0x80000000;

    /*
     * Command ID constants (CID prefix).
     */
    public static final int CID_BIND_RECEIVER = 0x00000001;
    public static final int CID_BIND_TRANSMITTER = 0x00000002;
    public static final int CID_QUERY_SM = 0x00000003;
    public static final int CID_SUBMIT_SM = 0x00000004;
    public static final int CID_DELIVER_SM = 0x00000005;
    public static final int CID_UNBIND = 0x00000006;
    public static final int CID_REPLACE_SM = 0x00000007;
    public static final int CID_CANCEL_SM = 0x00000008;
    public static final int CID_BIND_TRANSCEIVER = 0x00000009;
    public static final int CID_OUTBIND = 0x0000000B;
    public static final int CID_ENQUIRE_LINK = 0x00000015;
    public static final int CID_SUBMIT_MULTI = 0x00000021;
    public static final int CID_ALERT_NOTIFICATION = 0x00000102;
    public static final int CID_DATA_SM = 0x00000103;
    public static final int CID_BROADCAST_SM = 0x00000111;
    public static final int CID_QUERY_BROADCAST_SM = 0x00000112;
    public static final int CID_CANCEL_BROADCAST_SM = 0x00000113;
    public static final int CID_GENERIC_NACK = 0x80000000;
    public static final int CID_BIND_RECEIVER_RESP = 0x80000001;
    public static final int CID_BIND_TRANSMITTER_RESP = 0x80000002;
    public static final int CID_QUERY_SM_RESP = 0x80000003;
    public static final int CID_SUBMIT_SM_RESP = 0x80000004;
    public static final int CID_DELIVER_SM_RESP = 0x80000005;
    public static final int CID_UNBIND_RESP = 0x80000006;
    public static final int CID_REPLACE_SM_RESP = 0x80000007;
    public static final int CID_CANCEL_SM_RESP = 0x80000008;
    public static final int CID_BIND_TRANSCEIVER_RESP = 0x80000009;
    public static final int CID_ENQUIRE_LINK_RESP = 0x80000015;
    public static final int CID_SUBMIT_MULTI_RESP = 0x80000021;
    public static final int CID_DATA_SM_RESP = 0x80000103;
    public static final int CID_BROADCAST_SM_RESP = 0x80000111;
    public static final int CID_QUERY_BROADCAST_SM_RESP = 0x80000112;
    public static final int CID_CANCEL_BROADCAST_SM_RESP = 0x80000113;

    /*
     * Command Status constants (STAT prefix).
     */
    public static final int STAT_ESME_ROK = 0x00000000;
    public static final int STAT_ESME_RINVMSGLEN = 0x00000001;
    public static final int STAT_ESME_RINVCMDLEN = 0x00000002;
    public static final int STAT_ESME_RINVCMDID = 0x00000003;

    /**
     * Incorrect BIND Status for given command.
     * <p>
     * PDU has been sent in the wrong session state.<br>
     * E.g. sending a submit_sm without first establishing a Bound_TX session
     * state.
     */
    public static final int STAT_ESME_RINVBNDSTS = 0x00000004;

    /**
     * ESME already in Bound State.
     */
    public static final int STAT_ESME_RALYBND = 0x00000005;
    public static final int STAT_ESME_RINVPRTFLG = 0x00000006;
    public static final int STAT_ESME_RINVREGDLVFLG = 0x00000007;
    public static final int STAT_ESME_RSYSERR = 0x00000008;
    public static final int STAT_ESME_RINVSRCADR = 0x0000000A;
    public static final int STAT_ESME_RINVDSTADR = 0x0000000B;
    public static final int STAT_ESME_RINVMSGID = 0x0000000C;
    /**
     * Bind Failed.
     */
    public static final int STAT_ESME_RBINDFAIL = 0x0000000D;
    public static final int STAT_ESME_RINVPASWD = 0x0000000E;
    public static final int STAT_ESME_RINVSYSID = 0x0000000F;
    public static final int STAT_ESME_RCANCELFAIL = 0x00000011;
    public static final int STAT_ESME_RREPLACEFAIL = 0x00000013;
    /**
     * Message queue full.
     */
    public static final int STAT_ESME_RMSGQFUL = 0x00000014;
    public static final int STAT_ESME_RINVSERTYP = 0x00000015;
    public static final int STAT_ESME_RINVNUMDESTS = 0x00000033;
    public static final int STAT_ESME_RINVDLNAME = 0x00000034;
    public static final int STAT_ESME_RINVDESTFLAG = 0x00000040;
    public static final int STAT_ESME_RINVSUBREP = 0x00000042;
    public static final int STAT_ESME_RINVESMCLASS = 0x00000043;
    public static final int STAT_ESME_RCNTSUBDL = 0x00000044;
    public static final int STAT_ESME_RSUBMITFAIL = 0x00000045;
    public static final int STAT_ESME_RINVSRCTON = 0x00000048;
    public static final int STAT_ESME_RINVSRCNPI = 0x00000049;
    public static final int STAT_ESME_RINVDSTTON = 0x00000050;
    public static final int STAT_ESME_RINVDSTNPI = 0x00000051;
    public static final int STAT_ESME_RINVSYSTYP = 0x00000053;
    public static final int STAT_ESME_RINVREPFLAG = 0x00000054;
    public static final int STAT_ESME_RINVNUMMSGS = 0x00000055;
    public static final int STAT_ESME_RTHROTTLED = 0x00000058;
    public static final int STAT_ESME_RINVSCHED = 0x00000061;
    public static final int STAT_ESME_RINVEXPIRY = 0x00000062;
    /**
     * Predefined Message Invalid or Not Found.
     */
    public static final int STAT_ESME_RINVDFTMSGID = 0x00000063;
    /**
     * ESME Receiver Temporary App Error Code
     */
    public static final int STAT_ESME_RX_T_APPN = 0x00000064;
    /**
     * ESME Receiver Permanent App Error Code
     */
    public static final int STAT_ESME_RX_P_APPN = 0x00000065;
    /**
     * ESME Receiver Reject App Error Code
     */
    public static final int STAT_ESME_RX_R_APPN = 0x00000066;
    public static final int STAT_ESME_RQUERYFAIL = 0x00000067;
    public static final int STAT_ESME_RINVTLVSTREAM = 0x000000C0;
    public static final int STAT_ESME_RTLVNOTALLWD = 0x000000C1;
    public static final int STAT_ESME_RINVTLVLEN = 0x000000C2;
    public static final int STAT_ESME_RMISSINGTLV = 0x000000C3;
    public static final int STAT_ESME_RINVTLVVAL = 0x000000C4;
    public static final int STAT_ESME_RDELIVERYFAILURE = 0x000000FE;
    public static final int STAT_ESME_RUNKNOWNERR = 0x000000FF;
    public static final int STAT_ESME_RSERTYPUNAUTH = 0x00000100;
    public static final int STAT_ESME_RPROHIBITED = 0x00000101;
    public static final int STAT_ESME_RSERTYPUNAVAIL = 0x00000102;
    public static final int STAT_ESME_RSERTYPDENIED = 0x00000103;
    public static final int STAT_ESME_RINVDCS = 0x00000104;
    public static final int STAT_ESME_RINVSRCADDRSUBUNIT = 0x00000105;
    public static final int STAT_ESME_RINVDSTADDRSUBUNIT = 0x00000106;
    public static final int STAT_ESME_RINVBCASTFREQINT = 0x00000107;
    public static final int STAT_ESME_RINVBCASTALIAS_NAME = 0x00000108;
    public static final int STAT_ESME_RINVBCASTAREAFMT = 0x00000109;
    public static final int STAT_ESME_RINVNUMBCAST_AREAS = 0x0000010A;
    public static final int STAT_ESME_RINVBCASTCNTTYPE = 0x0000010B;
    public static final int STAT_ESME_RINVBCASTMSGCLASS = 0x0000010C;
    public static final int STAT_ESME_RBCASTFAIL = 0x0000010D;
    public static final int STAT_ESME_RBCASTQUERYFAIL = 0x0000010E;
    public static final int STAT_ESME_RBCASTCANCELFAIL = 0x0000010F;
    public static final int STAT_ESME_RINVBCAST_REP = 0x00000110;
    public static final int STAT_ESME_RINVBCASTSRVGRP = 0x00000111;
    public static final int STAT_ESME_RINVBCASTCHANIND = 0x00000112;

    /*
     * ESM class parameter constant
     */
    // ESME -> SMSC Messaging Mode.
    public static final byte ESMCLS_DEFAULT_MODE = 0x00;
    public static final byte ESMCLS_DATAGRAM_MODE = 0x01;
    public static final byte ESMCLS_FORWARD_MODE = 0x02;
    public static final byte ESMCLS_STORE_FORWARD = 0x03;

    /**
     * Encoded esm_class parameter. xx0000xx
     */
    public static final byte ESMCLS_DEFAULT_MESSAGE_TYPE = 0x00;

    // ESME -> SMSC Message Type.
    /**
     * Short Message contains ESME Delivery Acknowledgment. xx0010xx
     */
    public static final byte ESMCLS_ESME_DELIVERY_ACK = 0x08;
    /**
     * Short Message contains ESME Manual/User Acknowledgment. xx0100xx
     */
    public static final byte ESMCLS_ESME_MANUAL_ACK = 0x10;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -> ESME)
     * xx0001xx Short Message contains SMSC Delivery Receipt.
     */
    public static final byte ESMCLS_SMSC_DELIV_RECEIPT = 0x04;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -> ESME)
     * xx0001xx Short Message contains SME Delivery Acknowledgment.
     */
    public static final byte ESMCLS_SME_DELIV_ACK = 0x08;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -> ESME)
     * xx0100xx Short Message contains SME Manual/User Acknowledgment.
     */
    public static final byte ESMCLS_SME_MANUAL_ACK = 0x10;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -> ESME)
     * xx0110xx Short Message contains Conversation Abort (Korean CDMA).
     */
    public static final byte ESMCLS_CONV_ABORT = 0x18;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -> ESME)
     * xx1000xx Short Message contains Intermediate Delivery Notification.
     */
    public static final byte ESMCLS_INTRMD_DELIV_NOTIF = 0x20;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -> ESME)
     * 01xxxxxx UDHI Indicator set.
     */
    public static final byte ESMCLS_UDHI_INDICATOR_SET = 0x40;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -> ESME)
     * 10xxxxxx Reply Path.
     */
    public static final byte ESMCLS_REPLY_PATH = (byte)0x80;

    /**
     * Encoded esm_class parameter for deliver_sm or data_sm (SMSC -> ESME)
     * 11xxxxxx UDHI and Reply Path can use.
     */
    public static final byte ESMCLS_UDHI_REPLY_PATH = (byte)0xc0;

    /*
     * SMSC Delivery Receipt of registered delivery. bits 1 and 0
     */

    /**
     * No SMSC Delivery receipt. xxxxxx00
     */
    public static final byte REGDEL_SMSC_NO = 0x00;

    /**
     * SMSC Delivery Receipt requested where final delivery outcome is delivery
     * success or failure. xxxxxx01
     */
    public static final byte REGDEL_SMSC_SUCCESS_FAILURE_REQUESTED = 0x01;

    /**
     * SMSC Deliver Receipt requested where final delivery outcome is delivery
     * failure. xxxxxx10
     */
    public static final byte REGDEL_SMSC_FAILURE_REQUESTED = 0x02;

    /*
     * SME originated Acknowledgment of registered delivery. bits 3 and 2
     */

    /**
     * No recipient SME Acknowledgment requested. xxxx00xx
     */
    public static final byte REGDEL_SME_ACK_NO = 0x00;

    /**
     * SME Delivery Acknowledgment requested. xxxx01xx
     */
    public static final byte REGDEL_SME_DELIVERY_ACK_REQUESTED = 0x04;

    /**
     * SME Manual/User Acknowledgment requested. xxxx10xx
     */
    public static final byte REGDEL_SME_MANUAL_ACK_REQUESTED = 0x08;

    /**
     * Both Delivery and Manual/User Acknowledgment requested.
     */
    public static final byte REGDEL_SME_DELIVERY_MANUAL_ACK_REQUESTED = 0x0c;

    /*
     * Data Coding.
     */
    /**
     * SMSC Default Alphabet. 00000000
     */
    public static final byte DC_DEFAULT = 0x00;

    /**
     * Octet unspecified (8-bit binary)
     */
    public static final byte DC_BINARY = 0x04;

    /**
     * UCS2 (ISO/IEC-10646) 00001000
     */
    public static final byte DC_UCS2 = 0x08;

    /*
     * interface_version
     */
    public static final byte IF_VERSION_33 = 0x33;
    public static final byte IF_VERSION_34 = 0x34;
    public static final byte IF_VERSION_50 = 0x50;

    /*
     * Type of Number
     */
    public static final byte TON_UNKNOWN = 0x00;
    public static final byte TON_INTERNATIONAL = 0x01;
    public static final byte TON_NATIONAL = 0x02;
    public static final byte TON_NETWORK_SPECIFIC = 0x03;
    public static final byte TON_SUBSCRIBER_NUMBER = 0x04;
    public static final byte TON_ALPHANUMERIC = 0x05;
    public static final byte TON_ABBREVIATED = 0x06;

    /*
     * Numeric Plan Indicator
     */
    public static final byte NPI_UNKNOWN = 0x00;
    public static final byte NPI_ISDN = 0x01;
    public static final byte NPI_DATA = 0x03;
    public static final byte NPI_TELEX = 0x04;
    public static final byte NPI_LAND_MOBILE = 0x06;
    public static final byte NPI_NATIONAL = 0x08;
    public static final byte NPI_PRIVATE = 0x09;
    public static final byte NPI_ERMES = 0x0a;
    public static final byte NPI_INTERNET = 0x0e;
    public static final byte NPI_WAP = 0x12;

    public static final short TAG_SC_INTERFACE_VERSION = 0x0210;
    public static final short TAG_SAR_MSG_REF_NUM = 0X020C;
    public static final short TAG_SAR_TOTAL_SEGMENTS = 0x020e;
    public static final short TAG_SAR_SEGMENT_SEQNUM = 0x020f;

    /*
     * Data Coding constants
     */
    // Alphabet
    public static final byte MASK_ALPHA = 0x0f;
    public static final byte ALPHA_DEFAULT = 0x00; // 7-bit
    public static final byte ALPHA_UCS2 = 0x08;
}

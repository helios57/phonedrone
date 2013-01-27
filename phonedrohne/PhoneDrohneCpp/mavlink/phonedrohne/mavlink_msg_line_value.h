// MESSAGE LINE_VALUE PACKING

#define MAVLINK_MSG_ID_LINE_VALUE 1

typedef struct __mavlink_line_value_t
{
 int16_t pmw; ///< Value of line (PMW)
 uint8_t line; ///< Whith PD_LINE, line
} mavlink_line_value_t;

#define MAVLINK_MSG_ID_LINE_VALUE_LEN 3
#define MAVLINK_MSG_ID_1_LEN 3



#define MAVLINK_MESSAGE_INFO_LINE_VALUE { \
	"LINE_VALUE", \
	2, \
	{  { "pmw", NULL, MAVLINK_TYPE_INT16_T, 0, 0, offsetof(mavlink_line_value_t, pmw) }, \
         { "line", NULL, MAVLINK_TYPE_UINT8_T, 0, 2, offsetof(mavlink_line_value_t, line) }, \
         } \
}


/**
 * @brief Pack a line_value message
 * @param system_id ID of this system
 * @param component_id ID of this component (e.g. 200 for IMU)
 * @param msg The MAVLink message to compress the data into
 *
 * @param line Whith PD_LINE, line
 * @param pmw Value of line (PMW)
 * @return length of the message in bytes (excluding serial stream start sign)
 */
static inline uint16_t mavlink_msg_line_value_pack(uint8_t system_id, uint8_t component_id, mavlink_message_t* msg,
						       uint8_t line, int16_t pmw)
{
#if MAVLINK_NEED_BYTE_SWAP || !MAVLINK_ALIGNED_FIELDS
	char buf[3];
	_mav_put_int16_t(buf, 0, pmw);
	_mav_put_uint8_t(buf, 2, line);

        memcpy(_MAV_PAYLOAD_NON_CONST(msg), buf, 3);
#else
	mavlink_line_value_t packet;
	packet.pmw = pmw;
	packet.line = line;

        memcpy(_MAV_PAYLOAD_NON_CONST(msg), &packet, 3);
#endif

	msg->msgid = MAVLINK_MSG_ID_LINE_VALUE;
	return mavlink_finalize_message(msg, system_id, component_id, 3, 215);
}

/**
 * @brief Pack a line_value message on a channel
 * @param system_id ID of this system
 * @param component_id ID of this component (e.g. 200 for IMU)
 * @param chan The MAVLink channel this message was sent over
 * @param msg The MAVLink message to compress the data into
 * @param line Whith PD_LINE, line
 * @param pmw Value of line (PMW)
 * @return length of the message in bytes (excluding serial stream start sign)
 */
static inline uint16_t mavlink_msg_line_value_pack_chan(uint8_t system_id, uint8_t component_id, uint8_t chan,
							   mavlink_message_t* msg,
						           uint8_t line,int16_t pmw)
{
#if MAVLINK_NEED_BYTE_SWAP || !MAVLINK_ALIGNED_FIELDS
	char buf[3];
	_mav_put_int16_t(buf, 0, pmw);
	_mav_put_uint8_t(buf, 2, line);

        memcpy(_MAV_PAYLOAD_NON_CONST(msg), buf, 3);
#else
	mavlink_line_value_t packet;
	packet.pmw = pmw;
	packet.line = line;

        memcpy(_MAV_PAYLOAD_NON_CONST(msg), &packet, 3);
#endif

	msg->msgid = MAVLINK_MSG_ID_LINE_VALUE;
	return mavlink_finalize_message_chan(msg, system_id, component_id, chan, 3, 215);
}

/**
 * @brief Encode a line_value struct into a message
 *
 * @param system_id ID of this system
 * @param component_id ID of this component (e.g. 200 for IMU)
 * @param msg The MAVLink message to compress the data into
 * @param line_value C-struct to read the message contents from
 */
static inline uint16_t mavlink_msg_line_value_encode(uint8_t system_id, uint8_t component_id, mavlink_message_t* msg, const mavlink_line_value_t* line_value)
{
	return mavlink_msg_line_value_pack(system_id, component_id, msg, line_value->line, line_value->pmw);
}

/**
 * @brief Send a line_value message
 * @param chan MAVLink channel to send the message
 *
 * @param line Whith PD_LINE, line
 * @param pmw Value of line (PMW)
 */
#ifdef MAVLINK_USE_CONVENIENCE_FUNCTIONS

static inline void mavlink_msg_line_value_send(mavlink_channel_t chan, uint8_t line, int16_t pmw)
{
#if MAVLINK_NEED_BYTE_SWAP || !MAVLINK_ALIGNED_FIELDS
	char buf[3];
	_mav_put_int16_t(buf, 0, pmw);
	_mav_put_uint8_t(buf, 2, line);

	_mav_finalize_message_chan_send(chan, MAVLINK_MSG_ID_LINE_VALUE, buf, 3, 215);
#else
	mavlink_line_value_t packet;
	packet.pmw = pmw;
	packet.line = line;

	_mav_finalize_message_chan_send(chan, MAVLINK_MSG_ID_LINE_VALUE, (const char *)&packet, 3, 215);
#endif
}

#endif

// MESSAGE LINE_VALUE UNPACKING


/**
 * @brief Get field line from line_value message
 *
 * @return Whith PD_LINE, line
 */
static inline uint8_t mavlink_msg_line_value_get_line(const mavlink_message_t* msg)
{
	return _MAV_RETURN_uint8_t(msg,  2);
}

/**
 * @brief Get field pmw from line_value message
 *
 * @return Value of line (PMW)
 */
static inline int16_t mavlink_msg_line_value_get_pmw(const mavlink_message_t* msg)
{
	return _MAV_RETURN_int16_t(msg,  0);
}

/**
 * @brief Decode a line_value message into a struct
 *
 * @param msg The message to decode
 * @param line_value C-struct to decode the message contents into
 */
static inline void mavlink_msg_line_value_decode(const mavlink_message_t* msg, mavlink_line_value_t* line_value)
{
#if MAVLINK_NEED_BYTE_SWAP
	line_value->pmw = mavlink_msg_line_value_get_pmw(msg);
	line_value->line = mavlink_msg_line_value_get_line(msg);
#else
	memcpy(line_value, _MAV_PAYLOAD(msg), 3);
#endif
}

import React, { useEffect, useRef, useState } from "react";
import { EventBus } from "../EventBus.js";

export default function GameChat({ packetHandler }) {
    const [isOpen, setIsOpen] = useState(false);
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [note, setNote] = useState(false);
    const containerRef = useRef(null);

    useEffect(() => {
        const onMessage = (message) => {
            if (message.textType !== 'PLAYER') return;
            setMessages(m => [...m, message]);
            if (!isOpen) setNote(true);
        };
        EventBus.on('packet-TEXT_PACKET', onMessage);
        return () => EventBus.removeListener('packet-TEXT_PACKET', onMessage);
    }, [isOpen]);

    useEffect(() => {
        if (containerRef.current) containerRef.current.scrollTop = containerRef.current.scrollHeight;
    }, [messages]);

    const sendMessage = () => {
        if (message) packetHandler.sendPacket({
            type: 'TEXT_PACKET',
            text: message,
        });
        setMessage("");
    }

    const handleTyping = (e) => setMessage(e.target.value);
    const onInputKey = (e) => e.key === 'Enter' && sendMessage();
    const openChat = () => {
        setIsOpen(!isOpen);
        if (!isOpen) setNote(false);
    }

    const messagesComponent = (
        <div className="flex flex-col h-64 w-96 bg-blue-700 p-4 rounded-lg shadow-lg">
            <div className="flex-grow overflow-y-auto" ref={containerRef}>
                {messages.map((message, i) => (
                    <div key={i} className="mb-2">
                        <b>{message.sender}</b> &gt; {message.text}
                    </div>
                ))}
            </div>
            <div className="flex mt-2">
                <input
                    className="flex-grow rounded-l-lg px-2 py-1 text-black"
                    type="text"
                    placeholder="Сообщение"
                    value={message}
                    onChange={handleTyping}
                    onKeyDown={onInputKey}
                />
                <button
                    className="bg-pink-700 text-white rounded-r-lg px-2 py-1"
                    onClick={sendMessage}
                >
                    Отправить
                </button>
            </div>
        </div>
    );

    return (
        <div className="fixed bottom-4 right-4">
            {isOpen && messagesComponent}
            <button
                className={`bg-blue-700 text-white rounded-lg px-4 py-2 shadow-lg ${note ? "animate-bounce" : ""}`}
                onClick={openChat}
            >
                {isOpen ? "Закрыть" : "Открыть"} чат
            </button>
        </div>
    );
}

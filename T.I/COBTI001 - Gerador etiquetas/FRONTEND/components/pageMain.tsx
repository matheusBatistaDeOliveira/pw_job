'use client'
import localFont from 'next/font/local';
import { useState } from 'react';
import { FaLongArrowAltRight } from "react-icons/fa";
import { IoIosArrowBack } from "react-icons/io";
import Loader from '../components/Loader';
import { useGetData } from '../services/hooks/useGetData';
import "../assets/style/list.css"
import Swal from 'sweetalert2'
import Link from 'next/link';



const fontRegular = localFont({ src: '../assets/fonts/Poppins-Regular.ttf' });
const fontBold = localFont({ src: '../assets/fonts/Poppins-Bold.ttf' });
const fontExtraBold = localFont({ src: '../assets/fonts/Poppins-ExtraBold.ttf' });



interface labelCategory {
    ORIGEM: string
    DESC_ORIGEM: string
    NOTA: string
    NFI_SERIE: string
    AGENDA: string
    DATA: string
    LOJA_DESTINO: string
    DESC_DESTINO: string
}

export default function PageMain() {

    const gerarTextoEtiqueta = (option: labelCategory) => {
        const cleanDateString = option.DATA.substring(0, 10);
        const [ano, mes, dia] = cleanDateString.split("-");
        const formattedDate = `${dia}/${mes}/${ano}`;
        const today = new Date();
        const date = today.toLocaleDateString('pt-BR')
        return `
        
        ^XA
        
        ^CF0,70
^FO160,70^FDTRANSFERENCIA ^FS
^CF0,50
^FO50,160^FDORIGEM ^FS
^CF0,50
^FO50,210^FD${option.ORIGEM} ${option.DESC_ORIGEM.slice(4)}^FS
^CF0,50
^FO50,270^FDDESTINO ^FS
^CF0,50
^FO50,320^FD${option.LOJA_DESTINO}- ${option.DESC_DESTINO.slice(6)}^FS
^CF0,50
^FO50,420^FDNOTA- ^FS
^CF0,50
^FO200,420^FD${option.NOTA}^FS
^CF0,50
^FO50,470^FDSERIE- ^FS
^CF0,50
^FO210,470^FD${option.NFI_SERIE}^FS
^CF0,50
^FO50,520^FDEMISSAO NF- ^FS
^CF0,50
^FO350,520^FD${formattedDate}^FS
^CF0,50
^FO50,620^FDEMISSAO ETIQUETA- ^FS
^CF0,50
^FO493,620^FD${date} ^FS

^FO235,720^GFA,5208,5208,42,,:iW03E,iV01FFC,gT03IFEgW07FFE,gT07IFEgW0JF,:P01FFCg07IFEgV01JF8,O0LFY07IFEgV01JF8,N0NFX0JFEgV03JF8,M07NFCW0JFCgV03JF8,L03OFCW0JFCgV03JF8,L0PFCW0JFCgV03JF8,K03PFCW0JFCgV03JF8,K07PF8V01JFCgV03JF,J01QF8V01JF8gV01JF,J03QF8V01JF8gV01IFE,J0RFW01JF8gW0IFE,I01RFW01JF8gW07FF8,I03RFW03JF8gW03FF,I07QFEW03JFgY078,I0RFEW03JF,:001MFC003FCW03JF,003LFCJ01CW07JF,007KFEgJ07IFE,007KF8S01FF8M07IFE003F8Q01FFEO07FE,00LFS03JF8L07IFE03IFP07KF8L0KF,00KFER01LFL07IFE1JFCN03MF8J07KFEI07IF,01KF8R07LFCK0JFE7KFM01NFEI01MF800JFC,01KF8Q01MFEK0JFCLF8L07NFEI07MFI0JFC,03KFR03NF8J0QFCK01OFE001NF001JFC,03JFER0OFCJ0QFCK07OFE003NF001JF8,07JFCQ01OFEJ0QFEK0PFE007NF001JF8,07JFCQ03PFI01RFJ01PFE00NFE001JF8,07JF8Q07PF8001RFJ07PFC00NFE001JF8,0KF8Q0QF8001RF8I0QFC01NFE001JF8,0KFQ01QFC001RF8001QFC03NFC003JF,0KFQ03QFC001RF8003QFC03NFC003JF,0KFQ03QFE003RFC003QFC03NFC003JF,0JFEQ07KF8KFE003RFC007KFC3JF803JF800F8003JF,1JFEQ0KFC03KF003KFE1KFC00KFE01JF807JFI018003JF,1JFEQ0KF801KF003KF807JFC00KFC01JF807IFEM07JF,1JFEP01KFI0KF003JFE007JFC01KF001JF807JFM07IFE,1JFEP01JFEI07JF007JFC003JFC01JFE003JF807JF8L07IFE,1JFEP03JFCI07JF007JFC003JFC03JFC003JF807JFEL07IFE,1JFEP03JFCI07JF007JF8001JFC03JFC003JF007KFCK07IFE,1JFEP03JF8I07JF807JFI01JFC07JF8003JF007LFK0JFE,1JFEP07JF8I07JF807JFI01JFC07JFI03JF007LFCJ0JFC,1JFEP07JFJ07JF80JFEI01JFC0KFI03JF003MFJ0JFC,1JFEP07JFJ07JF00JFEI01JFC0JFEI07JF003MF8I0JFC,1JFEP07JFJ07JF00JFEI01JFC0JFEI07IFE001MFEI0JFC,1KFP07JFJ07JF00JFCI03JF80JFEI07IFEI0NFI0JFC,1KFP0JFEJ07JF00JFCI03JF81JFCI07IFEI0NF801JFC,0KF8O0JFEJ07JF00JFCI03JF81JFCI07IFEI03MF801JF8,0KF8O0JFEJ0KF01JFCI03JF81JFCI0JFEI01MFC01JF8,0KFCO0JFEJ0KF01JFCI07JF81JFCI0JFEJ0MFC01JF8,0KFCO0JFEJ0JFE01JFCI07JF01JFC001JFCJ03LFE01JF8,0KFEO0JFEI01JFE01JF8I0KF01JFC001JFCK0LFE01JF8,07KFO0JFEI01JFE01JF8I0JFE01JFC001JFCK03KFE03JF,07KFCN0JFEI03JFC01JF8001JFE01JFC003JFCL07JFE03JF,07KFEN07JFI03JFC03JF8003JFC01JFE007JFCL03JFE03JF,03LF8J01007JFI07JFC03JFC007JFC01JFE00KFCL01JFE03JF,03MFI01F807JF800KF803JFC00KF801KF01KFC01CJ0JFE07JF,01RF807JFC01KF803JFE01KF801KF83KFC01F8I0JFE07JF,01RF807KF0LF003KF0LF001RF803FF001JFE07IFE,00RF803RF003QFE001RF803JF7JFC07IFE,007QFC03QFE001QFEI0RF803OFC07IFE,003QFC01QFC001QFCI0RF807OFC07IFE,003QFC01QF8001QF8I0RF807OF80JFE,001QFC00QFI01QFJ07MF7IF807OF80JFC,I0QFC00PFEJ0PFEJ07LFE7IF80PF00JFC,I03PFC007OFCJ0PFCJ03LFCJF80OFE00JFC,I01PFC003OF8J07OFK03LFCJF80OFC00JFC,J0PFC001NFEK03NFEK01LF8JF81OF800JFC,J03OFEI07MFCK01NF8L0LF0JF80OF001JFC,K0OFCI03MFM0MFEM07JFE0JF807MFC001JF8,K03NFK0LFCM03LF8M01JF80JF801MFI01JF8,L03LF8K03JFEO0KFCO0IFE007IFI03KF8I01JF8,M03IFEN03FFEQ0IFCP01FFQ03IFCK0JF,,::::::::::gN06h0C,gN0F8J01EgR01FJ0F,01gL0F8J01EgR01FJ0F,03IFCgH0F8J01EgR01FJ0F,03IFCgH07K01EgS0EJ0F,03IFCgN01EgX0F,03CgQ01EgX0F,03CJ038006I03J018I07K03801EJ01CI0FJ0401CK07R020F007,03CI01FF83FF01FE01CFF003FE0703FF01EI0E7F807FC039E1FF8I07FEI0F0079E01FCF07FE,03CI03FF87FF83FF81IF807FF0F07FF81EI0IFC0IF03FE3FFCI0IFI0F8078E07IF0IF,03CI07FF8IF07EFC1IF80IF8F07FF81EI0IFE0IF03FE3FFEI0IF800780F0E0JF0IF8,03IF0F810F020F03C1F87C1F078F0607C1EI0FC1F0C0F83F0303EI040F800780F0E0F83F06078,03IF0FI0E001E01E1F03C3E030FI03C1EI0F80FI0783EI01EK078003C0E0E1F01FI03C,03IF0F800F001E00E1E01C3CI0FI01C1EI0F00FI0783CJ0EK03C003C1E0E1E01FI03C,03IF07F80FF01F01E1E01C3CI0F00FFC1EI0F00781FF83C003FEJ0FFC001E1E0E1E00F00FFC,03CI07FF07FE1IFE1E01C3CI0F03FFC1EI0E0078IF83C01FFEI07FFC001E3C0E1E00F07FFC,03CI01FF83FF1IFE1E01C3CI0F07FFC1EI0E0078IF83C03FFEI0IFC001E3C0E1E00F0IFC,03CJ03FC03F9EI01C01C3CI0F0F83C1EI0F0079E0783C07C1E001F03CI0F380E1E00F0F03C,03CK03C0079EI01E01C3CI0F0F01C1EI0F00F1C0383C0780E001E03CI0F780E1E01F1E03C,03CK03C0078FI01E01C1E070F0F03C1EI0F81F1C0783C0781E001E07CI07F00E1F03F1E03C,03EFE8F07CE078FC381E01C1F8F8F0F07C1EI0FC3E1E0F83C0783E001E0FCI07F00E0FC7F1F07C,03IFCIF9IF07FFC1E01C0IF8F0IFC1EI0IFE1IF83C07FFEI0IFCI03F00E07IF0IFC,03IFCIF0IF03FF81E01E07FF0F07FFC1EI0IFC0IF83C03FFEI0IFCI03E01E03IF07FFC,03IFC3FE07FC00FF01C01C01FC0703F9C1EI0E7F007F383C00FCEI03F38I01E00E01FCF03F1C,gY0E,::gY0F,gY0E,,:::^FS

^XZ`.trim();
    };
    const [keyLabel, setKeyLabel] = useState<labelCategory[]>([]);
    const [inputValueLabel, setInputValueLabel] = useState('');

    const [inputValueOrigin, setInputValueOrigin] = useState('');

    //campo que armazena a quantidade de impressoes 
    const [quantityLabel, setQuantityLabel] = useState('1');

    const [loading, setLoading] = useState(false);
    const { getLabel } = useGetData();
    const [showLabel, setShowLabel] = useState(false);

    const [showBox, setShowBox] = useState(false)

    const numbers = ['1', '2', '3', '4', '5',
        '6', '7', '8', '9', '10'
    ]

    // regra para palavra etiqueta
    const textLabel = +quantityLabel == 1 ? "etiqueta" : "etiquetas"
    const textSent = +quantityLabel == 1 ? "enviada" : "enviadas"

    const handleSubmit = async (e: any) => {
        e.preventDefault();
        setLoading(true);
        try {
            const labelRes = await getLabel(inputValueLabel, inputValueOrigin);
            //console.log('API Response for Chave/Nota:', labelRes);
            if (!labelRes.error) {
                setKeyLabel(labelRes);
                setShowLabel(true);
            } else {
                Swal.fire({
                    title: "Erro ao trazer os dados, entre em contato com T.I",
                    icon: "error",
                    draggable: true
                });
                console.log('Erro ao trazer os dados, entre em contato com T.I');
            }
        } catch (error) {
            Swal.fire({
                title: 'Erro ao buscar dados:', error,
                icon: "error",
                draggable: true
            });
            console.error('Erro ao buscar dados:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleTest = async (option: labelCategory) => {
        if (+quantityLabel <= 0) {
            alert("Quantidade de impressoes menor ou igual a 0, por favor coloque uma quantidade validade")
        } else {
            for (let i = 0; i < +quantityLabel; i++) {
                const text = gerarTextoEtiqueta(option);
                console.log(text)

            }
        }
    }

    const handlePrint = async (option: labelCategory) => {
        if (+quantityLabel <= 0) {
            Swal.fire({
                title: "A quantidade de impressões é menor ou igual a 0. Por favor, coloque uma quantidade válida.",
                icon: "error",
                draggable: true
            });
        } else {
            setLoading(true);
            try {
                const text = gerarTextoEtiqueta(option);
                console.log(text);

                for (let i = 0; i < +quantityLabel; i++) {
                    const response = await fetch('http://192.168.0.92:9090/api/print', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({
                            zpl: text,
                            printer: '\\\\10.11.50.34\\zebra_etiqueta_de_embarque_cd_jaguare'
                        })
                    });

                    if (response.status === 500) {
                        Swal.fire({
                            title: "Caminho da rede de impressão está desligado. Contate o setor de T.I.",
                            icon: "error",
                            draggable: true
                        });
                        setLoading(false);
                        return;
                    }
                }

                // Sucesso - fora do for
                Swal.fire({
                    title: `${quantityLabel} ${textLabel} ${textSent} para impressão.`,
                    icon: "success",
                    draggable: true
                });
                setLoading(false);

            } catch (error) {
                Swal.fire({
                    title: `Erro inesperado ao imprimir.`,
                    icon: "error",
                    draggable: true
                });
                setLoading(false);
            }

        }
    };

    const Swal = require('sweetalert2')

    if (loading) {
        return <Loader />
    }

    // aqui esta dando erro : quandoeu coloco a chave o sistema não consegue fazer a busca
    const format = (option: labelCategory) => {
        if (typeof option.DATA !== 'string' || option.DATA.length < 10) {
            return "Data Inválida";
        }
        const cleanDateString = option.DATA.substring(0, 10);
        const parts = cleanDateString.split("-");
        if (parts.length === 3) {
            const [ano, mes, dia] = parts;
            return `${dia}/${mes}/${ano}`;
        } else {
            return "Data Malformada";
        }
    };
    return (
        <div className="flex flex-row w-full h-full">
            <div className="w-[50%] h-[100vh] flex justify-center items-center" style={{ backgroundColor: '#fff', width: '50%' }} id='main'>
                <div className='absolute top-0 left-0 z-0'>
                    <img src="/shape001.svg" alt="" />
                </div>
                <div className='w-[87%] rounded-[30] p-10 flex flex-col gap-10 drop-shadow-2xl' style={{ backgroundColor: '#F2F2F2' }}>
                    <h1 className={`${fontBold.className} text-black text-1xl`}>
                        Coloque o número da nota ou chave
                    </h1>
                    <form onSubmit={handleSubmit} className='flex gap-10 flex-col'>
                        <input
                            type="number"
                            className={`${fontBold.className} outline-0 rounded-[30] pl-7 text-black w-[70%] font-bold`}
                            style={{ backgroundColor: '#D9D9D9', height: 40, }}
                            value={inputValueLabel}
                            required
                            onChange={(e) => setInputValueLabel(e.target.value)}
                        />

                        <h1 className={`${fontBold.className} text-black text-1xl`}>
                            Coloque o número da origem do cd ou loja com o dígito
                        </h1>

                        <input
                            type="number"
                            className={`${fontBold.className} outline-0 rounded-[30] pl-7 text-black w-[40%] font-bold`}
                            style={{ backgroundColor: '#D9D9D9', height: 40 }}
                            value={inputValueOrigin}
                            required
                            onChange={(e) => setInputValueOrigin(e.target.value)}
                        />

                        {showLabel && (
                            <div className='w-[70%] max-h-100 bg-white z-999 rounded-[30] shadow-md' style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)' }}>
                                <div className='pl-2 pt-2 cursor-pointer'
                                    style={{ width: '7%' }}
                                    onClick={() => {
                                        setShowLabel(false)
                                        setInputValueLabel('')
                                        setInputValueOrigin('')
                                        setQuantityLabel('1')
                                    }}>
                                    <IoIosArrowBack size={25} color='#000' />
                                </div>
                                {keyLabel.map((option, index) => (
                                    <div key={index} className=' p-3 flex flex-row flex-wrap justify-center gap-4 rounded-2xl'>
                                        <div className='flex flex-col gap-2 pb-2'>
                                            <div className='p-3 rounded-2xl flex items-center justify-center' style={{ backgroundColor: '#F2F2F2', height: 'auto', width: 'auto' }}>
                                                <p>Origem: {option.ORIGEM} {option.DESC_ORIGEM.slice(4)} <br />
                                                    Destino: {option.LOJA_DESTINO} - {option.DESC_DESTINO.slice(6)} <br />
                                                    Data nf emissão: {format(option)}
                                                </p>
                                            </div>

                                            <div className='flex flex-row justify-center'>
                                                <div className='flex flex-row justify-around'>
                                                    <div>
                                                        <h1
                                                            className={`${fontBold.className}`}
                                                        >Quantas etiquetas</h1>

                                                        <input
                                                            type="text"
                                                            readOnly={true}
                                                            className={`${fontRegular.className} outline-0 rounded-[30] pl-7 text-black w-[40%] font-bold`}
                                                            style={{ backgroundColor: '#D9D9D9', height: 40 }}
                                                            value={quantityLabel}
                                                            onChange={(e) => setQuantityLabel(e.target.value)}
                                                            onFocus={() => setShowBox(true)}
                                                            onBlur={() => setTimeout(() => setShowBox(false), 100)} />
                                                        {showBox && (
                                                            <ul className="absolute z-10 mt-[1] w-1/5 max-h-30 overflow-y-auto scroll bg-white border border-gray-300 rounded-xl shadow-lg">
                                                                {(numbers).map((option) => (
                                                                    <li

                                                                        onMouseDown={() => setQuantityLabel(option)}
                                                                        className="px-4 py-2 cursor-pointer hover:bg-gray-100"
                                                                        style={{ fontSize: 15, fontWeight: 'bold' }}
                                                                    >
                                                                        {option}
                                                                    </li>
                                                                ))}
                                                            </ul>
                                                        )}
                                                    </div>
                                                    <button
                                                        onClick={() => handlePrint(option)}
                                                        className={`${fontBold.className}w-[auto] h-[auto] p-3 rounded-2xl text-white cursor-pointer active:opacity-50`}
                                                        style={{ backgroundColor: '#7EB339' }}>
                                                        Imprimir
                                                    </button>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}



                        <button
                            type='submit'
                            className='rounded-[30] flex items-center justify-center cursor-pointer shadow-md active:opacity-50'
                            style={{ backgroundColor: '#7EB339', width: 100, height: 40 }}
                        >
                            <FaLongArrowAltRight color='#fff' size={28} />
                        </button>
                    </form>
                </div>
            </div>

            <div className="w-[50%] h-[100vh] flex flex-col pt-20 items-center gap-5" style={{ backgroundColor: '#009CA6' }} id='insad'>
                <img src="/title.svg" alt="" />
                <h1 className={`${fontBold.className} text-white text-2xl text-center`}>
                    Gerador de etiquetas <br />
                    de transferência
                </h1>
                <div className="absolute bottom-0 right-0 z-0">
                    <img
                        src="/catDog.svg"
                        alt=""
                        className="h-[68vh] w-auto object-contain"
                    />
                </div>

            </div>
        </div>
    );
}
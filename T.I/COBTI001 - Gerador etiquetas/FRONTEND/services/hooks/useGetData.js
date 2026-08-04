import { api } from "../../apis";

export const useGetData = () => {
    const getLabel = async (nota, origem) => {  
        if(nota.length == 44){
            try {
                const res = await api.get(`/buscarChave?chave=${nota}&origem=${origem}`);
                            return res.data;
                        } catch (error) {
                            console.log({ error });
                            return error;
                        }
        }else{
            try {
                const res = await api.get(`/buscarNota?nota=${nota}&origem=${origem}`);
                            return res.data;
                        } catch (error) {
                            console.log({ error });
                            return error;
                        }
        }
    };
    const postLabel = async (label) => {  
        const url = '/api/print';
        const data = {
            zpl: text,
            printer: '\\172.16.1.112\ZEBRA_CD_JAGUARE' 
        };
            try {
                const res = await api.post(url, data);
                            return res.data;
                        } catch (error) {
                            console.log({ error });
                            return error;
                        }
        
    };
    return {
        getLabel,
        postLabel
    };
};